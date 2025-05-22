package com.example.acc_project2_api_server.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final KafkaTemplate<String, Order> orderKt;
    private final KafkaTemplate<String, FailureEvent> failKt;
    private final MeterRegistry meter;
    private final BrokerClient broker;

    public OrderService(KafkaTemplate<String, Order> orderKt,
                        KafkaTemplate<String, FailureEvent> failKt,
                        MeterRegistry meter,
                        BrokerClient broker) {
        this.orderKt = orderKt;
        this.failKt  = failKt;
        this.meter   = meter;
        this.broker  = broker;
    }

    public void processOrder(Order order) {
        // 1) 항상 “orders” 토픽에 기록
        orderKt.send("orders", order);

        // 2) 브로커에 동기 호출
        try {
            broker.sendOrder(order).block();
        } catch (Exception ex) {
            // 3) 실패 메트릭 증가
            meter.counter("broker.failures", "broker", "broker1").increment();
            // 4) “broker-failure” 토픽에 실패 이벤트 Produce
            FailureEvent ev = new FailureEvent(
                    order.orderId(), "broker1", Instant.now(), ex.getMessage());
            failKt.send("broker-failure", ev);
        }
    }
}

