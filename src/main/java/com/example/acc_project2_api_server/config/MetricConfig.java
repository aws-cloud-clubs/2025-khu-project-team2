package com.example.acc_project2_api_server.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

//Metric 설정 부분
@Configuration
public class MetricConfig {
    //Clock이 연결이 안되어서 넣어줌
    @Bean
    public Clock micrometerClock() {
        return Clock.SYSTEM;
    }

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(
            CloudWatchConfig config,
            Clock clock,
            CloudWatchAsyncClient cloudWatchAsyncClient
    ) {
        // v2 레지스트리는 빌더 대신 생성자를 사용합니다
        return new CloudWatchMeterRegistry(config, clock, cloudWatchAsyncClient);
    }
    // CloudWatchConfig 구현체 필요 (region, credentials 등)
    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.of("ap-northeast-2"))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public CloudWatchConfig cloudWatchConfig() {
        return new CloudWatchConfig() {
            @Override public String get(String key)   { return null; }
            @Override public String namespace()       { return "MyApp"; }
        };
    }


}