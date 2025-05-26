package com.example.acc_project2_api_server.broker.sender;

import com.example.acc_project2_api_server.dto.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SubBrokerSender implements OrderSender {
    private final WebClient client;
    private static final String SUB_BROKER_URL = "http://sub-broker";

    public SubBrokerSender(WebClient.Builder wb) {
        this.client = wb.build();
    }

    @Override
    public Mono<Void> sendOrder(Order order) {
        return client.post()
                .uri(SUB_BROKER_URL + "/orders")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }
} 