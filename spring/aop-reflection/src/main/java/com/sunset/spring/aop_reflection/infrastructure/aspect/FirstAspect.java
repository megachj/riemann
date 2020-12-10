package com.sunset.spring.aop_reflection.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect @Order(value = 1)
@Component
public class FirstAspect {

}
