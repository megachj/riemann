package com.sunset.spring.resilence4j.springboot2;

import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerRegister;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.time.Duration;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.sunset.spring.resilience4j.*"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CircuitBreakerRegister.MyCircuitBreakerConfig.class})})
@Configuration
public class Resilience4jTestApplication {

    @Bean
    public CircuitBreakerConfig testCircuitBreakerConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        // 클라이언트 환경에 맞게 설정 변경
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.from(circuitBreakerRegistry.getDefaultConfig())
                        .slidingWindow(4, 2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                        .slowCallDurationThreshold(Duration.ofMillis(100)) // 지연시간 기준
                        .waitDurationInOpenState(Duration.ofSeconds(10)) // OPEN 에서 HALF_OPEN 으로 바뀌는 대기 시간, 추천 값: 수초 이내
                        .build();
        log.info("config: {}", circuitBreakerConfig);
        return circuitBreakerConfig;
    }
}
