package com.example.acc_project2_api_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//Metric 설정 부분
@Configuration
public class MetricsConfig {
    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config, Clock clock) {
        return CloudWatchMeterRegistry.builder(config).clock(clock).build();
    }
    // CloudWatchConfig 구현체 필요 (region, credentials 등)
}