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
    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config, Clock clock,  CloudWatchAsyncClient cloudWatchAsyncClient) {
        return CloudWatchMeterRegistry
                .builder(config, cloudWatchAsyncClient)      // ← 두 파라미터로 호출
                .clock(clock)
                .build();
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
            @Override public String get(String key)  { return null; } // default
            @Override public String namespace()      { return "MyApp"; }
        };
    }


}