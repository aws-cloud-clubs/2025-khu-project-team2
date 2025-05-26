package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

// Webflux webclient 사용
@Service
public class BrokerClient {
    private final WebClient client;
    private static final List<String> brokers = List.of("http://broker1", "http://broker2", "http://broker3");

    public BrokerClient(WebClient.Builder wb) {
        this.client = wb.build();
    }

    public Mono<Void> sendOrder(Order order ,String brokerUrl) {
        // 단순 라운드로빈 가능하긴 함 고민 좀 해봄 알고리즘 로직
        return client.post()
                .uri(brokerUrl + "/orders")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public List<String> getBrokers() {
        return brokers;
    }
}