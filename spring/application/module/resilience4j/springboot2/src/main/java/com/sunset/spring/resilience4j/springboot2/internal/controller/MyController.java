package com.sunset.spring.resilience4j.springboot2.internal.controller;

import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteClient;
import io.micrometer.core.instrument.MeterRegistry;
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
    private final MeterRegistry meterRegistry;

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

    public enum Type {
        success {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.doSuccess();
            }
        },
        ignoreException {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.doIgnoreException();
            }
        },
        exception {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.doException();
            }
        },
        latency {
            @Override
            public String remoteCall(RemoteClient remoteClient) {
                return remoteClient.doLatency();
            }
        };

        public abstract String remoteCall(RemoteClient remoteClient);
    }
}
