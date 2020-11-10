package com.sunset.spring.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommandController {

    private final AsyncService asyncService;

    @PostMapping("/command/run")
    public Boolean commandDoTask(@RequestParam(defaultValue = "1000") long millis) {
        log.info("command do task async start.");
        asyncService.pingCheckAsync(millis);

        log.info("return http response.");
        return true;
    }
}
