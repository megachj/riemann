package com.sunset.spring.resilience4j.springboot2.internal.client;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.MyCircuitBreakerConfig;
import com.sunset.spring.resilience4j.springboot2.internal.exception.IgnoreException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteClient {

    private final RemoteCallService remoteCallService;

    @CircuitBreaker(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doSuccess")
    public String doSuccess() {
        return remoteCallService.doSuccess();
    }
    private String doSuccess(Throwable ex) {
        String eMessage = "It's doSuccess fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doIgnoreException")
    public String doIgnoreException() {
        try {
            return remoteCallService.doIgnoreException();
        } catch (HttpClientErrorException ex) {
            throw new IgnoreException("클라 에러는 무시", ex);
        }
    }
    private String doIgnoreException(Throwable ex) {
        String eMessage = "It's doIgnoreException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doException")
    public String doException() {
        return remoteCallService.doException();
    }
    private String doException(Throwable ex) {
        String eMessage = "It's doException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doLatency")
    public String doLatency() {
        return remoteCallService.doLatency();
    }
    private String doLatency(Throwable ex) {
        String eMessage = "It's doLatency fallback method.";
        log.warn(eMessage);
        return eMessage;
    }
}
