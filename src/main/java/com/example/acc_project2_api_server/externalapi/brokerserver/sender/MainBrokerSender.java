package com.example.acc_project2_api_server.externalapi.brokerserver.sender;

import com.example.acc_project2_api_server.dto.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MainBrokerSender implements OrderSender {
    private final WebClient client;
    // TODO: URL 나중에 수정
    private static final String MAIN_BROKER_URL = "http://main-broker";

    public MainBrokerSender(WebClient.Builder wb) {
        this.client = wb.build();
    }

    @Override
    public Mono<Void> sendOrder(Order order) {
        return client.post()
                .uri(MAIN_BROKER_URL + "/orders")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }
} 