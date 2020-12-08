package com.sunset.spring.aop_reflection.reflection;

public class TestMain {

    public static void main(String[] args) throws ClassNotFoundException {
        // 클래스 정보
        Class<MyServiceImpl> myServiceClass = (Class<MyServiceImpl>) Class.forName("com.sunset.spring.aop_reflection.reflection.MyServiceImpl");


    }
}
