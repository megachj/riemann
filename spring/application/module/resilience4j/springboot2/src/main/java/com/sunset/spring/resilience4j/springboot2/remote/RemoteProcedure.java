package com.sunset.spring.resilience4j.springboot2.remote;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/remote")
public class RemoteProcedure {

    @GetMapping("/success")
    public String getSuccess() {
        return "SUCCESS";
    }

    @GetMapping("/client-exception")
    public String getClientException() {
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/server-exception")
    public String getException() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/latency")
    public String getFailureServer() {
        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {}

        return "LATENCY OK";
    }

    @GetMapping("/chaos-monkey")
    public String getChaosMonkey() {
        return "OK";
    }
}
