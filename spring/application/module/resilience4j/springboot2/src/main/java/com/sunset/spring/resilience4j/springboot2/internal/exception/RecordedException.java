package com.sunset.spring.resilience4j.springboot2.internal.exception;

public class RecordedException extends RuntimeException {
    public RecordedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
