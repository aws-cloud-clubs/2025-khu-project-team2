package com.example.acc_project2_api_server.controller;

import com.example.acc_project2_api_server.dto.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/sub-broker-order")
@RequiredArgsConstructor
public class SubBrokerOrderController {
    private static final int MAX_REQUESTS = 10;
    private final AtomicInteger requestCount = new AtomicInteger();

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        int current = requestCount.incrementAndGet();

        System.out.println("SubBrokerOrderController 요청 도착: " + order);

        if (current > MAX_REQUESTS) {
            System.out.println("SubBroker 최대 요청 초과 - 에러 발생");
            throw new RuntimeException("SubBroker 서버 과부하 - 더 이상 처리 불가");
        }

        return ResponseEntity.accepted().build();
    }
}
