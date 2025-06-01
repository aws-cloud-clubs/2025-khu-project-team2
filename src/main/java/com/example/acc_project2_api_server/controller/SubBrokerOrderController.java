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
@RequestMapping("/sub-broker-order")
@RequiredArgsConstructor
public class SubBrokerOrderController {
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        return ResponseEntity.accepted().build();  // 무조건 정상 응답
    }
}
