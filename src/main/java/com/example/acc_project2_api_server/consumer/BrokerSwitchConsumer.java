package com.example.acc_project2_api_server.consumer;

import com.example.acc_project2_api_server.externalapi.brokerserver.BrokerClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BrokerSwitchConsumer {
    private final BrokerClient brokerClient;

    public BrokerSwitchConsumer(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;
    }
  
    // TODO: 토픽과 그룹은 나중에 정해야한다
    @KafkaListener(topics = "broker-switch-events", groupId = "broker-switch-group")
    public void consumeBrokerSwitchEvent() {
        brokerClient.switchBroker();
    }
} 