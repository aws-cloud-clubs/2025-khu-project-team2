package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.FailureEvent;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetryService {

    private final BrokerClient brokerClient;
    private final KafkaTemplate<String, FailureEvent> failureKt;
    private final MeterRegistry meterRegistry;

    public void retryOrder(FailureEvent failureEvent) {
        try{
            String targetBroker = pickNextBroker(failureEvent.getFailedBroker());
            brokerClient.sendToBroker(failureEvent.getOrderId(), targetBroker());

        }
        catch (Exception ex){
            meterRegistry.counter("broker.retry.failures", "broker", failureEvent.getFailedBroker()).increment();
            failureKt.send("broker-failure", failureEvent);
        }
    }
}
