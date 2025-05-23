package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.Order;
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
        // 단순 라운드로빈 가능하긴 함 고민 좀 해봄 알고리즘 로직
        return client.post()
                .uri(brokers.get(0) + "/orders")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }
}