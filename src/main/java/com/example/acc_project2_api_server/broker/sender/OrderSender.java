package com.example.acc_project2_api_server.broker.sender;

import com.example.acc_project2_api_server.dto.Order;
import reactor.core.publisher.Mono;

public interface OrderSender {
    Mono<Void> sendOrder(Order order);
} 