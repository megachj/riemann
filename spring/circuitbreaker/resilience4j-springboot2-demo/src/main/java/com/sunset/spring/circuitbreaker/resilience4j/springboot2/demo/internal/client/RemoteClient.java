package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RemoteClient {
    private final WebClient webClient;

    public RemoteClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
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
