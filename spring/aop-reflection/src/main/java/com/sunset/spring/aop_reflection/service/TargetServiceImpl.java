package com.sunset.spring.aop_reflection.service;

import com.sunset.spring.aop_reflection.infrastructure.annotation.MyMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class TargetServiceImpl implements TargetService {

    @MyMethod
    @Override
    public int hello(String greeting, List<String> names) {
        Assert.notNull(names, "names must be not null.");
        names.forEach(name -> {
            System.out.println(greeting + ": " + name);
        });
        return names.size();
    }
}
