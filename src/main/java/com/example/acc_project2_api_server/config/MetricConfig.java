package com.example.acc_project2_api_server.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;

//Metric 설정 부분
@Configuration
public class MetricConfig {
    @Value("${aws.region}")
    private String awsRegion;

    @Value("${management.metrics.export.cloudwatch.namespace}")
    private String namespace;
    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry() {
        return new CloudWatchMeterRegistry (cloudWatchConfig(), clock(), cloudWatchAsyncClient()); // no builder
    }
    // CloudWatchConfig 구현체 필요 (region, credentials 등)

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    @Bean
    public CloudWatchConfig cloudWatchConfig() {
        return new CloudWatchConfig() {
            @Override public String get(String key)  { return null; } // default
            @Override public String namespace()      { return namespace; }
            @Override
            public Duration step() {
                return Duration.ofMillis(1); // 1분 주기로 전송
            }

            @Override
            public boolean enabled() {
                return true;
            }
        };
    }

    @Bean
    public Clock clock() {
        return Clock.SYSTEM; // 메트릭의 시간 측정 기준 시간으로 이용
    }
}