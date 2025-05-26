package com.example.acc_project2_api_server.consumer;

import com.example.acc_project2_api_server.broker.client.BrokerClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BrokerSwitchConsumer {
    private final BrokerClient brokerClient;

    public BrokerSwitchConsumer(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;
    }

    @KafkaListener(topics = "broker-switch-events", groupId = "broker-switch-group")
    public void consumeBrokerSwitchEvent() {
        brokerClient.switchBroker();
    }
} 