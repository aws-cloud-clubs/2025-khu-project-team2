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
@RequestMapping("/broker-order")
@RequiredArgsConstructor
public class BrokerOrderController {
    private static final int MAX_REQUESTS = 10; //에러 발생 조건 -> 나중에 수정
    private final AtomicInteger requestCount = new AtomicInteger();

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        int current = requestCount.incrementAndGet();
        System.out.println("BrokerOrderController 요청 도착: " + order);
        // TODO: 확률적으로 에러를 발생시켜야한다
        if (current > MAX_REQUESTS) {
            System.out.println("최대 요청 초과 - 에러 발생");
            throw new RuntimeException("서버 과부하 - 더 이상 처리할 수 없습니다.");
        }
        return ResponseEntity.accepted().build();
    }
}
