package com.sunset.spring.bean;

import org.springframework.stereotype.Service;

@Service
public class MyService implements MyServiceSpec {
    @Override
    public void hello() {
        System.out.println("Hello MyService!");
    }
}
