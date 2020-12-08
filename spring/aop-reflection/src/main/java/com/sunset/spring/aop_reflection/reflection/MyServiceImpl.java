package com.sunset.spring.aop_reflection.reflection;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyServiceImpl implements MyService {

    private String message;

    private int code;

    @MyClassAnnotation
    @Override
    public void print(String iString, int iInt, long iLong) {
        System.out.printf("Print, %s, %d, %d\n", iString, iInt, iLong);
    }
}
