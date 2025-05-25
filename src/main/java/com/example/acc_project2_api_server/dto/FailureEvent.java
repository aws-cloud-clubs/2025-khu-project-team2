package com.example.acc_project2_api_server.dto;

import lombok.Data;

import java.time.Instant;

public record FailureEvent(
        Order order,

        String failedBroker,
        Instant timestamp,
        String reason
)
{}
