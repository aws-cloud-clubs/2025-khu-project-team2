package com.example.acc_project2_api_server.controller;

import com.example.acc_project2_api_server.dto.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/broker-order")
@RequiredArgsConstructor
public class BrokerOrderController {
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        // TODO: 확률적으로 에러를 발생시켜야한다
        return ResponseEntity.accepted().build();
    }
}
