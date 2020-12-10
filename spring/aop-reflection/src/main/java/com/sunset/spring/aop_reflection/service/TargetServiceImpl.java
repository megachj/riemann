package com.sunset.spring.aop_reflection.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TargetServiceImpl implements TargetService {

    @Override
    public boolean hello(String greeting, List<String> names) {
        return false;
    }
}
