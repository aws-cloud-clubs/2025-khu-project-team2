package com.example.acc_project2_api_server.dto;

import lombok.Data;


public record Order (
    String orderId,
    String itemId,
    int qty
){}
