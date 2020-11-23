package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
