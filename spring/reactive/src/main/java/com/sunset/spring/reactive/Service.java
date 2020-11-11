package com.sunset.spring.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.Future;

@Slf4j
@Component
public class Service {
    private final WebClient webClient;

    public Service(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @Async
    public Future<Boolean> callToSpringAsync(long millis) {
        log.info("callToSpringAsync begin. {} millis", millis);
        return new AsyncResult<>(callCommonApiTemplate(millis).block());
    }

    @Async("asyncTaskExecutor")
    public Future<Boolean> callToSpringAsyncTaskExecutor(long millis) {
        log.info("callToSpringAsync begin. {} millis", millis);
        return new AsyncResult<>(callCommonApiTemplate(millis).block());
    }


    public Future<Boolean> callToFuture(long millis) {
        log.info("callToFuture begin. {} millis", millis);
        return callCommonApiTemplate(millis).toFuture();
    }

    public Mono<Boolean> callToMonoUnsubscribe(long millis) {
        log.info("callToMonoUnsubscribe begin. {} millis", millis);
        return callCommonApiTemplate(millis);
    }

    public void callToMonoSubscribe(long millis) {
        log.info("callToMonoSubscribe begin. {} millis", millis);
        callCommonApiTemplate(millis).subscribe();
    }

    public boolean callToBlock(long millis) {
        log.info("callToBlock begin. {} millis", millis);
        return callCommonApiTemplate(millis).block();
    }

    private Mono<Boolean> callCommonApiTemplate(long millis) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/sleep").queryParam("millis", millis).build())
                .retrieve()
                .toEntity(String.class)
                .map(r -> {
                    log.info("Response: {}", r);
                    return true;
                });
    }

    public enum MethodType {
        SPRING_ASYNC {
            @Override
            public void callMethod(Service service, long millis) {
                service.callToSpringAsync(millis);
            }
        },
        SPRING_ASYNC_TASK_EXECUTOR {
            @Override
            public void callMethod(Service service, long millis) {
                service.callToSpringAsyncTaskExecutor(millis);
            }
        },
        FUTURE {
            @Override
            public void callMethod(Service service, long millis) {
                service.callToFuture(millis);
            }
        },
        MONO_UNSUBSCRIBE {
            @Override
            public void callMethod(Service service, long millis) {
                service.callToMonoUnsubscribe(millis);
            }
        },
        MONO_SUBSCRIBE {
            @Override
            public void callMethod(Service service, long millis) {
                service.callToMonoSubscribe(millis);
            }
        },
        BLOCK {
            @Override
            public void callMethod(Service service, long millis) {
                service.callToBlock(millis);
            }
        };

        public abstract void callMethod(Service service, long millis);
    }
}
