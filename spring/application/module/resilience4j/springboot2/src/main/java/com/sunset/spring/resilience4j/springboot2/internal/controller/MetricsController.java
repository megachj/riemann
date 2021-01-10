package com.sunset.spring.resilience4j.springboot2.internal.controller;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.MyCircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/circuitbreaker")
    public ResponseEntity<CircuitBreaker.Metrics> getCircuitBreakerMetrics() {
        if (circuitBreakerRegistry.getAllCircuitBreakers().size() == 0) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(circuitBreakerRegistry.circuitBreaker(MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME).getMetrics(), HttpStatus.OK);
    }
}
