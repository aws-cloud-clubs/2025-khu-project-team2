package com.example.acc_project2_api_server.broker.client;

import com.example.acc_project2_api_server.broker.sender.OrderSender;
import com.example.acc_project2_api_server.broker.sender.MainBrokerSender;
import com.example.acc_project2_api_server.broker.sender.SubBrokerSender;
import com.example.acc_project2_api_server.dto.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class BrokerClient {
    public enum BrokerType {
        MAIN, SUB
    }

    private BrokerType currentBrokerType;
    private final Map<BrokerType, OrderSender> senders;

    public BrokerClient(MainBrokerSender mainBroker, SubBrokerSender subBroker) {
        this.senders = Map.of(
                BrokerType.MAIN, mainBroker,
                BrokerType.SUB, subBroker
        );
        this.currentBrokerType = BrokerType.MAIN;
    }

    public Mono<Void> sendOrder(Order order) {
        OrderSender sender = senders.get(currentBrokerType);
        return sender.sendOrder(order);
    }

    public void switchBroker() {
        BrokerType newType = (currentBrokerType == BrokerType.MAIN) ? BrokerType.SUB : BrokerType.MAIN;
        currentBrokerType = newType;
    }
}