package com.example.acc_project2_api_server.controller;

import com.example.acc_project2_api_server.dto.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/broker-order")
@RequiredArgsConstructor
public class BrokerOrderController {
    @Value("${broker.sub.max-requests}")
    private int maxRequests;

    private final AtomicInteger requestCount = new AtomicInteger();

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        int current = requestCount.incrementAndGet();
        // TODO: 확률적으로 에러를 발생시켜야한다
        if (current > maxRequests) {
            System.out.println("최대 요청 초과 - 에러 발생");
            throw new RuntimeException("서버 과부하 - 더 이상 처리할 수 없습니다.");
        }
        return ResponseEntity.accepted().build();
    }
}
