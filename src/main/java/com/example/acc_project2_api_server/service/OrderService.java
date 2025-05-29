package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.FailureEvent;
import com.example.acc_project2_api_server.dto.Order;
import com.example.acc_project2_api_server.exception.APITimeoutException;
import com.example.acc_project2_api_server.externalapi.brokerserver.BrokerClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class OrderService {
    private final KafkaTemplate<String, Order> orderKt;
    private final KafkaTemplate<String, FailureEvent> failKt;
    private final MeterRegistry meter;
    private final BrokerClient broker;

    // 메트릭스 인스턴스들
    private final Counter orderProcessedCounter; //성공 요청 수
    //private final Timer orderProcessingTimer; // 서비스 보장 수치 여부에 따라 추가 가능
    public OrderService(KafkaTemplate<String, Order> orderKt,
                        KafkaTemplate<String, FailureEvent> failKt,
                        MeterRegistry meter,
                        BrokerClient broker) {
        this.orderKt = orderKt;
        this.failKt  = failKt;
        this.meter   = meter;
        this.broker  = broker;

        // CloudWatch 성공 전용 메트릭스 초기화
        this.orderProcessedCounter = Counter.builder("order.processed.total")
                .description("Total number of orders processed")
                .tag("service", "api-server")
                .tag("component", "order-processor")
                .register(meter);
    }

    public void processOrder(Order order) {
        try {
            // 1) 항상 “orders” 토픽에 기록
            orderKt.send("orders", order).get(3, TimeUnit.SECONDS);

            try {
                broker.sendOrder(order);
                orderProcessedCounter.increment();
            } catch (Exception ex) {
                // 3) 실패 메트릭 증가
                meter.counter("broker.failures",
                                "service", "api-server",
                                "component", "order-processor",
                                "broker", "broker1")
                        .increment();
                // 4) “broker-failure” 토픽에 실패 이벤트 Produce
                FailureEvent ev = new FailureEvent(
                        order, "broker1", Instant.now(), ex.getMessage());
                failKt.send("broker-failure", ev);
            }
        } catch (TimeoutException te) {
            // 3) "API 시간 초과 실패"로 간주
            meter.counter("kafka.failures",
                            "service", "api-server",
                            "component", "order-processor",
                            "reason", "timeout")
                    .increment();

            // 4) failure topic에 알림 메시지 produce
            failKt.send("broker-failure", new FailureEvent(
                    order, "timeout", Instant.now(), te.getMessage()));

            throw new APITimeoutException("timed out", te);
        } catch (Exception kafkaEx) {
            // Kafka에 orders 토픽 전송 실패 시
            meter.counter("kafka.failures",
                            "service", "api-server",
                            "component", "order-processor",
                            "reason", kafkaEx.getClass().getSimpleName())
                    .increment();
            double cnt = meter.counter("kafka.failures",
                    "service", "api-server",
                    "component", "order-processor",
                    "reason", kafkaEx.getClass().getSimpleName()
            ).count();
            System.err.println("Failed to publish order to 'orders' topic: "
                    + kafkaEx.getMessage());
            System.out.println("[CloudWatch] kafka.failure.total = " + cnt);

            throw new RuntimeException("Kafka failure", kafkaEx);
        }
    }
}