package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.Order;
import com.example.acc_project2_api_server.externalapi.brokerserver.BrokerClient;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final MeterRegistry meter;
    private final BrokerClient broker;

    public OrderService(
                        MeterRegistry meter,
                        BrokerClient broker) {
        this.meter   = meter;
        this.broker  = broker;
    }

    public void processOrder(Order order) {
        // TODO: 메트릭 수집 로직이 구현되면 수정해야한다
//        broker.switchBroker(); //서브브로커 테스트
        broker.sendOrder(order)
            .doOnError(ex -> meter.counter("broker.failures", "broker", "broker1").increment())
            .subscribe();
    }
}

