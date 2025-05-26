package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.Order;
import com.example.acc_project2_api_server.broker.client.BrokerClient;
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
        broker.sendOrder(order)
            .doOnError(ex -> meter.counter("broker.failures", "broker", "broker1").increment())
            .subscribe();
    }
}

