package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.FailureEvent;
import com.example.acc_project2_api_server.dto.Order;
import com.example.acc_project2_api_server.externalapi.brokerserver.BrokerClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

@Service
public class OrderService {
    private final KafkaTemplate<String, Order> orderKt;
    private final KafkaTemplate<String, FailureEvent> failKt;
    private final MeterRegistry meter;
    private final BrokerClient broker;
    private final Counter orderProcessedCounter;

    public OrderService(KafkaTemplate<String, Order> orderKt,
                        KafkaTemplate<String, FailureEvent> failKt,
                        MeterRegistry meter,
                        BrokerClient broker) {
        this.orderKt = orderKt;
        this.failKt = failKt;
        this.meter = meter;
        this.broker = broker;

        this.orderProcessedCounter = Counter.builder("order.processed.total")
                .description("Total number of orders processed")
                .register(meter);
    }

    public void processOrder(Order order) {
        // Kafka 'orders' 토픽으로 비동기 전송하고 Mono로 변환
        Mono.fromFuture(orderKt.send("orders", order))
                .timeout(Duration.ofSeconds(3))
                .flatMap(sendResult ->
                        broker.sendOrder(order)
                                .doOnSuccess(v -> orderProcessedCounter.increment()) // Broker 성공 시 카운터 증가
                                .doOnError(brokerEx -> { // Broker 전송 실패 시
                                    meter.counter("broker.failures", "broker", "broker1").increment();
                                    failKt.send("broker-failure", new FailureEvent(order, "broker1", Instant.now(), brokerEx.getMessage()));
                                })
                                .onErrorResume(brokerEx -> Mono.empty())//evnet 2번 전송 막음
                )
                .doOnError(kafkaEx -> { // Kafka 전송 실패 시 (타임아웃 포함)
                    String reason = (kafkaEx instanceof TimeoutException) ? "timeout" : kafkaEx.getClass().getSimpleName();
                    meter.counter("kafka.failures", "reason", reason).increment();
                    failKt.send("broker-failure", new FailureEvent(order, "kafka-" + reason, Instant.now(), kafkaEx.getMessage()));
                })
                .subscribe(
                        null,
                        error -> System.err.println("Order processing failed for order ID " + order.orderId() + ": " + error.getMessage()) // 에러 로깅
                );
    }
}