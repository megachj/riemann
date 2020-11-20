package com.sunset.spring.circuitbreaker.spring_cloud_circuitbreaker.reactive.resilience4j;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DemoController {

    Logger LOG = LoggerFactory.getLogger(DemoController.class);

    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;
    private final HttpBinService httpBinService;

    @GetMapping("/get")
    public Mono<Map> get() {
        return httpBinService.get();
    }

    @GetMapping("/delay/{seconds}")
    public Mono<Map> delay(@PathVariable int seconds) {
        return circuitBreakerFactory.create("delay").run(httpBinService.delay(seconds), t -> {
            LOG.warn("delay call failed error", t);
            Map<String, String> fallback = new HashMap<>();
            fallback.put("hello", "world");
            return Mono.just(fallback);
        });
    }

    @GetMapping("/fluxdelay/{seconds}")
    public Flux<String> fluxDelay(@PathVariable int seconds) {
        return circuitBreakerFactory.create("delay").run(httpBinService.fluxDelay(seconds), t -> {
            LOG.warn("delay call failed error", t);
            return Flux.just("hello", "world");
        });
    }
}
