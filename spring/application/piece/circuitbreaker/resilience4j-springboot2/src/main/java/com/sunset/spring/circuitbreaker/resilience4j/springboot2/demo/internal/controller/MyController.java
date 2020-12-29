package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.controller;

import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.client.RemoteClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

@Slf4j
@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final RemoteClient remoteClient;
    private final ExecutorService executorService;

    /**
     * curl 명령어: curl -X GET "http://localhost:8080/my/service?type={type}"
     *
     * @param type
     * @return
     */
    @GetMapping("/service")
    public String service(@RequestParam Type type) {
        log.info("my service, type[{}]", type);
        return type.remoteCall(remoteClient);
    }

    @GetMapping("/service/chaos")
    public String serviceChaos(@RequestParam(defaultValue = "100") long millis) {
        log.info("my service chaos, millis[{}]", millis);
        executorService.submit(() -> {
            for ()
        });
    }

    public enum Type {
        success {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.success();
            }
        },
        failureClient {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.failureClient();
            }
        },
        failureServer {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.failureServer();
            }
        };

        public abstract String remoteCall(RemoteClient remoteClient);
    }
}
