package com.sunset.spring.resilence4j.springboot2.client;

import com.sunset.spring.resilence4j.springboot2.Resilience4jTestApplication;
import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerUtils;
import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.MyCircuitBreakerConfig;
import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteCallService;
import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteClient;
import com.sunset.spring.resilience4j.springboot2.internal.exception.IgnoreException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.Duration;

import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {Resilience4jTestApplication.class})
@Import(RemoteClientCircuitBreakerTest.TestCircuitBreakerConfig.class)
public class RemoteClientCircuitBreakerTest {

    @TestConfiguration
    public static class TestCircuitBreakerConfig {
        @Bean(name = MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_CONFIG_BEAN_NAME)
        public CircuitBreakerConfig testCircuitBreakerConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
            CircuitBreakerConfig circuitBreakerConfig =
                    CircuitBreakerConfig.from(circuitBreakerRegistry.getDefaultConfig())
                            .slidingWindow(4, 4, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                            .slowCallDurationThreshold(Duration.ofMillis(50)) // 지연시간 기준
                            .waitDurationInOpenState(Duration.ofSeconds(60)) // OPEN 에서 HALF_OPEN 으로 바뀌는 대기 시간
                            .ignoreExceptions(IgnoreException.class) // 무시할 예외 리스트
                            .build();
            return circuitBreakerConfig;
        }
    }

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RemoteClient remoteClient;
    @MockBean
    private RemoteCallService remoteCallService;

    private CircuitBreaker circuitBreaker;
    private CircuitBreaker.Metrics metrics;

    @Before
    public void initStubbing() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker(MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME);
        metrics = circuitBreaker.getMetrics();

        // given
        when(remoteCallService.doSuccess()).thenReturn("OK");
        when(remoteCallService.doIgnoreException()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(remoteCallService.doException()).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        when(remoteCallService.doLatency()).thenAnswer(invocation -> {
            try {
                Thread.sleep(100);
            } catch (Exception ignored) {}
            return "LATENCY OK";
        });
    }

    @Test
    public void openStateFallback_exception_test() {
        // when & then
        // 1. 상태 변화: CLOSED -> OPEN
        for (int i = 0; i < circuitBreaker.getCircuitBreakerConfig().getMinimumNumberOfCalls(); ++i) {
            remoteClient.doException();
        }
        Assertions.assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(2);
        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        // 2. OPEN 상태일 때 요청 허용되지 않음
        remoteClient.doSuccess();
        remoteClient.doSuccess();
        Assertions.assertThat(metrics.getNumberOfNotPermittedCalls()).isEqualTo(2); // 요청이 허용되지 않음.
        Assertions.assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(0); // 성공 요청 없음.
    }

    @Test
    public void openStateFallback_latency_test() {
        remoteClient.doSuccess();
        CircuitBreakerUtils.printStatusInfo(circuitBreaker);

        remoteClient.doException();
        CircuitBreakerUtils.printStatusInfo(circuitBreaker);

        remoteClient.doIgnoreException();
        CircuitBreakerUtils.printStatusInfo(circuitBreaker);

        remoteClient.doLatency();
        CircuitBreakerUtils.printStatusInfo(circuitBreaker);
    }
}
