package com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker;

@FunctionalInterface
public interface CircuitBreakerInitializer {
    void init();
}
