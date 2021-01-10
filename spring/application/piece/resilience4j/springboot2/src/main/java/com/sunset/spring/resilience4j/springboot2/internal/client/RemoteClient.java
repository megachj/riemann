package com.sunset.spring.resilience4j.springboot2.internal.client;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerRegister;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteClient {

    private final RemoteCallService remoteCallService;

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doSuccess")
    public String doSuccess() {
        return remoteCallService.doSuccess();
    }

    private String doSuccess(Throwable ex) {
        String eMessage = "It's doSuccess fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doIgnoreException")
    public String doIgnoreException() {
        return remoteCallService.doIgnoreException();
    }

    private String doIgnoreException(Throwable ex) {
        String eMessage = "It's doIgnoreException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doException")
    public String doException() {
        return remoteCallService.doException();
    }

    private String doException(Throwable ex) {
        String eMessage = "It's doException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doLatency")
    public String doLatency() {
        return remoteCallService.doLatency();
    }

    private String doLatency(Throwable ex) {
        String eMessage = "It's doLatency fallback method.";
        log.warn(eMessage);
        return eMessage;
    }
}
