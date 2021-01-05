package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class CircuitBreakerRegister {

    public static final String REMOTE_CIRCUIT_BREAKER_NAME = "remoteClientCircuitBreaker";

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void initRemoteClientCircuitBreaker() {
        // 클라이언트 환경에 맞게 설정 변경
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.from(circuitBreakerRegistry.getDefaultConfig())
                        .build();

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(REMOTE_CIRCUIT_BREAKER_NAME, circuitBreakerConfig);
        log.info("register circuitBreaker '{}'", circuitBreaker.getName());
    }
}
