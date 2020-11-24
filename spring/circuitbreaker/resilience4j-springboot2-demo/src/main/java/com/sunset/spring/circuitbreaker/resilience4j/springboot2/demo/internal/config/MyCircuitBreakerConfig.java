package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.config;

import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.exception.IgnoreException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MyCircuitBreakerConfig {

    public static final String REMOTE_CLIENT_CIRCUIT_BREAKER = "RemoteCircuitBreaker";

    @Bean
    public CircuitBreakerConfig defaultCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 실패율 threshold
                .slowCallRateThreshold(50) // 지연율 threshold
                .slowCallDurationThreshold(Duration.ofSeconds(1)) // 지연시간 기준
                .permittedNumberOfCallsInHalfOpenState(2) // HALF_OPEN 일 때 허용 콜 수
                .waitDurationInOpenState(Duration.ofSeconds(60)) // OPEN 에서 HALF_OPEN 으로 바뀌는 대기 시간
                .slidingWindowSize(4)
                .ignoreExceptions(IgnoreException.class) // 무시할 예외
                .build();
    }

    @Bean
    public CircuitBreakerRegistry myCircuitBreakerRegistry(CircuitBreakerConfig defaultCircuitBreakerConfig) {
        return CircuitBreakerRegistry.of(defaultCircuitBreakerConfig);
    }

    @Bean("CircuitBreaker-1")
    public CircuitBreaker remoteClientCircuitBreaker(CircuitBreakerRegistry myCircuitBreakerRegistry) {
        return myCircuitBreakerRegistry.circuitBreaker(REMOTE_CLIENT_CIRCUIT_BREAKER);
    }
}
