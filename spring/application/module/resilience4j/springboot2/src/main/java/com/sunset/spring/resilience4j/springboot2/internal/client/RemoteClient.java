package com.sunset.spring.resilience4j.springboot2.internal.client;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.MyCircuitBreakerConfig;
import com.sunset.spring.resilience4j.springboot2.internal.exception.IgnoredException;
import com.sunset.spring.resilience4j.springboot2.internal.exception.RecordedException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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

    @CircuitBreaker(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doException")
    public String doException(int code) {
        try {
            return remoteCallService.doException(code);
        } catch (HttpClientErrorException ex) {
            throw new IgnoredException("클라 에러는 무시", ex);
        } catch (HttpServerErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_GATEWAY)
                throw new IllegalStateException("배드게이트웨이는 고려되지 않음.");
            else
                throw new RecordedException("서버 에러는 기록", ex);
        }
    }
    private String doException(int code, CallNotPermittedException ex) {
        String eMessage = "It's doException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doLatency")
    public String doLatency() {
        return remoteCallService.doLatency();
    }
    private String doLatency(CallNotPermittedException ex) {
        String eMessage = "It's doLatency fallback method.";
        log.warn(eMessage);
        return eMessage;
    }
}
