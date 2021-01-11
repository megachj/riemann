package com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MyCircuitBreakerConfig {

    public static final String REMOTE_CIRCUIT_BREAKER_NAME = "remoteClientCircuitBreaker";
    public static final String REMOTE_CIRCUIT_BREAKER_CONFIG_BEAN_NAME = "remoteClientCircuitBreaker";

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    // 클라이언트 환경에 맞게 설정 변경
    @ConditionalOnMissingBean(name = REMOTE_CIRCUIT_BREAKER_CONFIG_BEAN_NAME)
    @Bean(name = REMOTE_CIRCUIT_BREAKER_CONFIG_BEAN_NAME)
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.from(circuitBreakerRegistry.getDefaultConfig())
                .build();
    }

    @Bean(initMethod = "init")
    public CircuitBreakerInitializer circuitBreakerInitializer(@Qualifier(REMOTE_CIRCUIT_BREAKER_CONFIG_BEAN_NAME) CircuitBreakerConfig circuitBreakerConfig) {
        return () -> {
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(REMOTE_CIRCUIT_BREAKER_NAME, circuitBreakerConfig);
            circuitBreaker.getEventPublisher()
                    .onSuccess(event -> {
                        log.info("circuitbreaker success: {}", event);
                    })
                    .onError(event -> {
                        log.info("circuitbreaker error: {}", event);
                    })
                    .onIgnoredError(event -> {
                        log.info("circuitbreaker ignoredError: {}", event);
                    })
                    .onReset(event -> {
                        log.info("circuitbreaker reset: {}", event);
                    })
                    .onStateTransition(event -> {
                        log.info("circuitbreaker stateTransition: {}", event);
                    });
            log.info("register circuitBreaker '{}'. config: {}", circuitBreaker.getName(), circuitBreakerConfig);
        };
    }
}
