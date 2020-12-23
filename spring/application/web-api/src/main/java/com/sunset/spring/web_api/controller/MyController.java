package com.sunset.spring.web_api.controller;

import com.sunset.spring.web_api.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/api/v1")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/member/{id}")
    public String getMemberName(@PathVariable("id") int id) {
        return null;
    }
}
