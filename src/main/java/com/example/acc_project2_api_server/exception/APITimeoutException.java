package com.example.acc_project2_api_server.exception;

public class APITimeoutException extends RuntimeException {
    public APITimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}