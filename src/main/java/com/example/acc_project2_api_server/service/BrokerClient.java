package com.example.acc_project2_api_server.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrokerClient {
    private final WebClient client;
    private final List<String> brokers = List.of("http://broker1", "http://broker2", "http://broker3");

    public BrokerClient(WebClient.Builder wb) {
        this.client = wb.build();
    }

    public Mono<Void> sendOrder(Order order) {
        // 단순 라운드로빈 or CircuitBreaker 로직 적용 가능
        return client.post()
                .uri(brokers.get(0) + "/orders")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }
}