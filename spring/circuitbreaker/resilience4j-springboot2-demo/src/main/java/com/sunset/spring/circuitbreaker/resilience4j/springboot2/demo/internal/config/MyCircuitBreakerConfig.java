package com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.config;

import com.sunset.spring.circuitbreaker.resilience4j.springboot2.demo.internal.exception.IgnoreException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class MyCircuitBreakerConfig {

    public static final String REMOTE_CLIENT_CIRCUIT_BREAKER = "RemoteCircuitBreaker";

    /*
    CircuitBreakerRegistry 타입은 스프링 빈이 오직 1개여야 한다. 그렇지 않으면 애플리케이션 기동 시에 아래 에러가 발생한다.
      - Parameter 0 of method circuitBreakerAspect in io.github.resilience4j.circuitbreaker.autoconfigure.AbstractCircuitBreakerConfigurationOnMissingBean required a single bean, but n were found:
     */
    @Bean("uniqueRegistry")
    public CircuitBreakerRegistry uniqueRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(defaultCircuitBreakerConfig());
        remoteClientCircuitBreaker(registry);
        return registry;
    }

    // 서킷브레이커 레지스트리 기본 설정
    public CircuitBreakerConfig defaultCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 실패율 threshold
                .slowCallRateThreshold(50) // 지연율 threshold
                .slowCallDurationThreshold(Duration.ofSeconds(1)) // 지연시간 기준
                .permittedNumberOfCallsInHalfOpenState(2) // HALF_OPEN 일 때 허용 콜 수
                .waitDurationInOpenState(Duration.ofSeconds(60)) // OPEN 에서 HALF_OPEN 으로 바뀌는 대기 시간
                // waitDurationInOpenState 기간 이후에 ScheduledExecutorSerivce를 이용해 half-open으로 자동으로 전환해줄지 결정하는 값.
                // 내부적으로 CircuitBreakerStateMachine 의 scheduledExecutorService 에 스케줄링할 스레드를 하나 등록하게 된다.
                .automaticTransitionFromOpenToHalfOpenEnabled(false)
                .slidingWindowSize(2)
                .ignoreExceptions(IgnoreException.class) // 무시할 예외
                .build();
    }

    // 서킷브레이커는 스프링 빈일 필요가 없다. 왜냐하면 CircuitBreakerRegistry 가 관리하기 때문이다.
    private void remoteClientCircuitBreaker(CircuitBreakerRegistry uniqueRegistry) {
        // registry 에 지정한 이름의 CircuitBreaker 가 없으면 생성후 리턴, 있으면 registry 에 있는 CircuitBreaker 리턴
        CircuitBreaker circuitBreaker =  uniqueRegistry.circuitBreaker(REMOTE_CLIENT_CIRCUIT_BREAKER);

        // 서킷브레이커 이벤트 컨슈머 등록
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> {
                    log.info("{}, successEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onError(event -> {
                    log.info("{}, errorEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onIgnoredError(event -> {
                    log.info("{}, ignoredErrorEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onReset(event -> {
                    log.info("{}, resetEvent: {}", event.getCircuitBreakerName(), event);
                })
                .onStateTransition(event -> {
                    log.info("{}, stateTransitionEvent: {}", event.getCircuitBreakerName(), event);
                });
    }
}
