package com.sunset.spring.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WorkController {

    @PostMapping("/task")
    public String doTask(@RequestParam(defaultValue = "1000") long millis) {
        try { Thread.sleep(millis); } catch (Exception ignored) {}
        log.info("Task success. [{}]ms", millis);
        return "OK";
    }
}
