package com.sunset.spring.resilience4j.springboot2.internal.client;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerRegister;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class RemoteClient {
    private final WebClient webClient;

    public RemoteClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doSuccess")
    public String doSuccess() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/success").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String doSuccess(Throwable ex) {
        String eMessage = "It's doSuccess fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doIgnoreException")
    public String doIgnoreException() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/client-exception").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String doIgnoreException(Throwable ex) {
        String eMessage = "It's doIgnoreException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doException")
    public String doException() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/server-exception").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String doException(Throwable ex) {
        String eMessage = "It's doException fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "doLatency")
    public String doLatency() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/latency").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String doLatency(Throwable ex) {
        String eMessage = "It's doLatency fallback method.";
        log.warn(eMessage);
        return eMessage;
    }

    /*@CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "chaosMonkeyFallback")
    public String chaosMonkey() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/chaos-monkey").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String chaosMonkeyFallback(Throwable ex) {
        String eMessage = "RemoteClient Circuit is in open state. it's chaosMonkeyFallback.";
        log.warn(eMessage);
        return eMessage;
    }*/
}
