package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.remote;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/remote")
public class RemoteProcedure {

    @GetMapping("/success")
    public String getSuccess() {
        return "SUCCESS";
    }

    @GetMapping("/failure/client")
    public String getFailureClient() {
        throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "This is a remote client exception. bad request.");
    }

    @GetMapping("/failure/server")
    public String getFailureServer() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote server exception. internal server error.");
    }

    @GetMapping("/chaos-monkey")
    public String getChaosMonkey() {
        return "OK";
    }
}
