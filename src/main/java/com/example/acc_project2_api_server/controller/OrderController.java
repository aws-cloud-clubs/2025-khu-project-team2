package com.example.acc_project2_api_server.controller;

import com.example.acc_project2_api_server.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService svc;


    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        svc.processOrder(order);
        return ResponseEntity.accepted().build();
    }
}

