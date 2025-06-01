package com.example.acc_project2_api_server.externalapi.brokerserver.sender;

import com.example.acc_project2_api_server.dto.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SubBrokerSender implements OrderSender {
    private final WebClient client;
    // TODO: URL 나중에 수정 
    private final String subBrokerUrl;

    public SubBrokerSender(WebClient.Builder wb, @Value("${broker.sub-url}") String url) {
        this.client = wb.build();
        this.subBrokerUrl = url;
    }

    @Override
    public Mono<Void> sendOrder(Order order) {
        return client.post()
                .uri(subBrokerUrl)
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class);
    }
} 