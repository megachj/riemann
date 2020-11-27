package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.client;

import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.exception.IgnoreException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = RemoteClientWithCircuitBreaker.REMOTE_CLIENT_CIRCUIT_BREAKER)
@Slf4j
@Component
public class RemoteClientWithCircuitBreaker {

    public static final String REMOTE_CLIENT_CIRCUIT_BREAKER = "remoteCircuitBreaker";

    private final WebClient webClient;

    public RemoteClientWithCircuitBreaker(WebClient.Builder webClientBuilder, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
        initCircuitBreaker(circuitBreakerRegistry);
    }

    private void initCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        // registry 에 지정한 이름의 CircuitBreaker 가 없으면 생성후 리턴, 있으면 registry 에 있는 CircuitBreaker 리턴
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(REMOTE_CLIENT_CIRCUIT_BREAKER, CircuitBreakerConfig.from(circuitBreakerRegistry.getDefaultConfig())
                .failureRateThreshold(50)
                .build());

        // 서킷브레이커 이벤트 컨슈머 등록
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> {
                    log.info("{}, successEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onError(event -> {
                    log.info("{}, errorEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onIgnoredError(event -> {
                    log.info("{}, ignoredErrorEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onReset(event -> {
                    log.info("{}, resetEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onStateTransition(event -> {
                    log.info("{}, stateTransitionEvent: {}", event.getCircuitBreakerName(), event);
                });
    }

    public Mono<String> getSuccess() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/success").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                });
    }

    public Mono<String> getFailureClient() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/failure/client").build())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class, ex -> {
                    log.warn("Response Error Message: {}", ex.getMessage());
                    return new IgnoreException();
                })
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                });
    }

    public Mono<String> getFailureServer() {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/failure/server").build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                });
    }

    public Mono<String> getSlow(long millis) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/remote/slow").queryParam("millis", millis).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return r;
                });
    }
}
