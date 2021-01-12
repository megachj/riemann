package com.sunset.spring.resilience4j.springboot2.internal.exception;

public class IgnoreException extends RuntimeException {
    public IgnoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
