package com.example.acc_project2_api_server.dto;

import java.time.Instant;

public record FailureEvent(String orderId,
                           String failedBroker,
                           Instant timestamp,
                           String reason) {

}
