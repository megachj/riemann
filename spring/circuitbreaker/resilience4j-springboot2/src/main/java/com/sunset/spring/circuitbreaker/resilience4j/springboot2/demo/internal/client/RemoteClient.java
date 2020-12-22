package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.client;

import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.circuitbreaker.CircuitBreakerRegister;
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

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "successFallback")
    public String success() {
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

    private String successFallback(Throwable ex) {
        String eMessage = "RemoteClient Circuit is in open state. it's successFallback.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "failureClientFallback")
    public String failureClient() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/failure/client").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String failureClientFallback(Throwable ex) {
        String eMessage = "RemoteClient Circuit is in open state. it's failureClientFallback.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "failureServerFallback")
    public String failureServer() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/failure/server").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                })
                .block();
    }

    private String failureServerFallback(Throwable ex) {
        String eMessage = "RemoteClient Circuit is in open state. it's failureServerFallback.";
        log.warn(eMessage);
        return eMessage;
    }

    @CircuitBreaker(name = CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME, fallbackMethod = "chaosMonkeyFallback")
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
    }
}
