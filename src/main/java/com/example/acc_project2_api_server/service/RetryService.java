package com.example.acc_project2_api_server.service;

import com.example.acc_project2_api_server.dto.FailureEvent;
import com.example.acc_project2_api_server.dto.Order;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetryService {

    private final BrokerClient brokerClient;
    private final KafkaTemplate<String, FailureEvent> failureKt;
    private final MeterRegistry meterRegistry;
// 재주문 메소드
    public void retryOrder(FailureEvent failureEvent) {
        try {
            // 실패한 브로커는 제외하고 다음 브로커 결정
            String targetBroker = pickNextBroker(failureEvent.failedBroker());
            // 변수 그대로 넘겨야 컴파일 에러 없이 호출됩니다
            brokerClient.sendOrder(failureEvent.order(), targetBroker)
                    .subscribe(
                            null,
                            ex -> handleRetryFailure(failureEvent)
                    );
        }
        catch (Exception ex) {
            // 실패 카운터 증가
            handleRetryFailure(failureEvent);
        }
    }
    private void handleRetryFailure(FailureEvent evt) {
        meterRegistry
                .counter("broker.retry.failures", "broker", evt.failedBroker())
                .increment();
        failureKt.send("broker-failure", evt);
    }


    // 다음 브로커 찾는 메소드
    private String pickNextBroker(String failedBroker) {
        List<String> allBrokers = List.of("http://broker1", "http://broker2", "http://broker3");
        return allBrokers.stream()
                .filter(b -> !b.equals(failedBroker))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("사용 가능한 브로커가 없습니다"));
    }
}
