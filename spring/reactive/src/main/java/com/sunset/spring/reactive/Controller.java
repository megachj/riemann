package com.sunset.spring.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Controller {

    private final Service service;

    /**
     * 동작 확인용 API
     * curl 명령어: curl -d 'methodType={methodType}&millis={millis}' -X POST 'http://localhost:8080/task/run'
     *
     * @param methodType
     * @param millis
     * @return
     */
    @PostMapping("/task/run")
    public Boolean runTask(@RequestParam("methodType") Service.MethodType methodType, @RequestParam(defaultValue = "1000") long millis) {
        log.info("runTask API start.");
        methodType.callMethod(service, millis);
        log.info("runTask API end.");

        return true;
    }

    @PostMapping("/sleep")
    public String sleep(@RequestParam(defaultValue = "1000") long millis) {
        long start = System.currentTimeMillis();
        try { Thread.sleep(millis); } catch (Exception ignored) {}
        log.info("sleep. [{}]ms", System.currentTimeMillis() - start);

        return "OK";
    }
}
