package com.sunset.spring.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.Future;

@Slf4j
@Component
public class AsyncService {
    private final WebClient webClient;

    public AsyncService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @Async
    public Future<Boolean> pingCheckAsync(long millis) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/task").queryParam("millis", millis).build())
                .retrieve()
                .toEntity(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return true;
                })
                .toFuture();
    }
}
