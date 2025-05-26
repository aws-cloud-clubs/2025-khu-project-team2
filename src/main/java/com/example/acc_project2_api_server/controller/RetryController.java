package com.example.acc_project2_api_server.controller;

import com.example.acc_project2_api_server.dto.FailureEvent;
import com.example.acc_project2_api_server.service.RetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//
@RestController
@RequestMapping("/retries")
@RequiredArgsConstructor
public class RetryController {

    private final RetryService retryService;

    @PostMapping
    public ResponseEntity<Void> retry(@RequestBody FailureEvent failureEvent) {
        retryService.retryOrder(failureEvent);
        return ResponseEntity.ok().build();
    }
}
