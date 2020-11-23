package com.sunset.spring.circuitbreaker.demo.resilience4j.springboot2.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
