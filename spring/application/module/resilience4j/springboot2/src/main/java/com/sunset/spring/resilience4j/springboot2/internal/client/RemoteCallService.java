package com.sunset.spring.resilience4j.springboot2.internal.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class RemoteCallService {

    private final WebClient webClient;

    public RemoteCallService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

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
}
