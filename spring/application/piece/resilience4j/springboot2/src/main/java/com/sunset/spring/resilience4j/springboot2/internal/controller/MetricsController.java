package com.sunset.spring.resilience4j.springboot2.internal.controller;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerRegister;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/circuitbreaker")
    public ResponseEntity<CircuitBreaker.Metrics> getCircuitBreakerMetrics() {
        return new ResponseEntity<>(circuitBreakerRegistry.circuitBreaker(CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME).getMetrics(), HttpStatus.OK);
    }
}
