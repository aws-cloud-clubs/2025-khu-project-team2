package com.example.acc_project2_api_server.externalapi.brokerserver.sender;

import com.example.acc_project2_api_server.dto.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MainBrokerSender implements OrderSender {
    private final WebClient client;
    // TODO: URL 나중에 수정
    private final String mainBrokerUrl;

    public MainBrokerSender(WebClient.Builder wb, @Value("${broker.main-url}") String url) {
        this.client = wb.build();
        this.mainBrokerUrl = url;
    }

    @Override
    public Mono<Void> sendOrder(Order order) {
        return client.post()
                .uri(mainBrokerUrl)
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }
} 