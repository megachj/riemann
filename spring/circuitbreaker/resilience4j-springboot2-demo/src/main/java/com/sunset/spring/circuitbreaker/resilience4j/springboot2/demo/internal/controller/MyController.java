package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.controller;

import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.client.RemoteClient;
import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.client.RemoteClientWithCircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final RemoteClient remoteClient;
    private final RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker;

    /**
     * curl 명령어: curl -X GET 'http://localhost:8080/my/remote-status?type={type}'
     *
     * @param type
     * @return
     */
    @GetMapping("/remote-status")
    public Mono<String> getRemoteStatus(@RequestParam Type type) {
        log.info("getRemoteStatus, type[{}]", type);
        return type.getRemoteCall(remoteClient, remoteClientWithCircuitBreaker);
    }

    public enum Type {
        SUCCESS {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClient.getSuccess();
            }
        },
        FAILURE_CLIENT {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClient.getFailureClient();
            }
        },
        FAILURE_SERVER {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClient.getFailureServer();
            }
        },
        SLOW {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClient.getSlow(3_000);
            }
        },
        CB_SUCCESS {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClientWithCircuitBreaker.getSuccess();
            }
        },
        CB_FAILURE_CLIENT {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClientWithCircuitBreaker.getFailureClient();
            }
        },
        CB_FAILURE_SERVER {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClientWithCircuitBreaker.getFailureServer();
            }
        },
        CB_SLOW {
            @Override
            public Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker) {
                return remoteClientWithCircuitBreaker.getSlow(3_000);
            }
        };

        public abstract Mono<String> getRemoteCall(RemoteClient remoteClient, RemoteClientWithCircuitBreaker remoteClientWithCircuitBreaker);
    }
}
