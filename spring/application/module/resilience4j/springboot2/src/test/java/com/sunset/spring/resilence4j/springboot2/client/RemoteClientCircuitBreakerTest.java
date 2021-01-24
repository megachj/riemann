package com.sunset.spring.resilence4j.springboot2.client;

import com.sunset.spring.resilence4j.springboot2.Resilience4jTestApplication;
import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerInitializer;
import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerUtils;
import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.MyCircuitBreakerConfig;
import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteCallService;
import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteClient;
import com.sunset.spring.resilience4j.springboot2.internal.exception.IgnoredException;
import com.sunset.spring.resilience4j.springboot2.internal.exception.RecordedException;
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
                            .ignoreExceptions(IgnoredException.class) // ignoreExceptions 가 가장 우선순위가 높다.
                            .recordExceptions(RecordedException.class)
                            .recordException(ex -> false) // 이 옵션이 있어야 recordExceptions 만 record(fail) 처리된다. 나머지는 모두 success 처리.
                            .build();
            return circuitBreakerConfig;
        }
    }

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    @Autowired
    private CircuitBreakerInitializer circuitBreakerInitializer;

    @Autowired
    private RemoteClient remoteClient;
    @MockBean
    private RemoteCallService remoteCallService;

    private CircuitBreaker circuitBreaker;
    private CircuitBreaker.Metrics metrics;

    @Before
    public void initStubbing() {
        circuitBreakerRegistry.remove(MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME);
        circuitBreakerInitializer.init();

        circuitBreaker = circuitBreakerRegistry.circuitBreaker(MyCircuitBreakerConfig.REMOTE_CIRCUIT_BREAKER_NAME);
        metrics = circuitBreaker.getMetrics();

        // given
        when(remoteCallService.doSuccess()).thenReturn("OK");
        when(remoteCallService.doException(400)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(remoteCallService.doException(500)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        when(remoteCallService.doException(502)).thenThrow(new HttpServerErrorException(HttpStatus.BAD_GATEWAY));
        when(remoteCallService.doLatency()).thenAnswer(invocation -> {
            try {
                Thread.sleep(100);
            } catch (Exception ignored) {}
            return "LATENCY OK";
        });
    }

    @Test
    public void openStateFallback_exception_test() {
        // when: 실패율로 상태 변화
        Assertions.catchThrowable(() -> remoteClient.doException(400)); // ignore, CLOSED
        Assertions.catchThrowable(() -> remoteClient.doException(400)); // ignore, CLOSED
        remoteClient.doSuccess(); // success, CLOSED
        remoteClient.doSuccess(); // success, CLOSED
        Assertions.catchThrowable(() -> remoteClient.doException(502)); // success, CLOSED
        Assertions.catchThrowable(() -> remoteClient.doException(502)); // success, CLOSED
        Assertions.catchThrowable(() -> remoteClient.doException(500)); // fail, CLOSED
        Assertions.catchThrowable(() -> remoteClient.doException(500)); // fail, CLOSED -> OPEN
        remoteClient.doSuccess(); // not permitted, OPEN
        CircuitBreakerUtils.printStatusInfo(circuitBreaker);

        // then
        Assertions.assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(4);
        Assertions.assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(2);
        Assertions.assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(2);
        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        Assertions.assertThat(metrics.getNumberOfNotPermittedCalls()).isEqualTo(1); // 요청이 허용되지 않음.
    }

    @Test
    public void openStateFallback_latency_test() {
        // when: 지연율로 상태 변화
        remoteClient.doSuccess(); // success, CLOSED
        remoteClient.doSuccess(); // success, CLOSED
        remoteClient.doLatency(); // slow success, CLOSED
        remoteClient.doLatency(); // slow success, CLOSED -> OPEN
        remoteClient.doSuccess(); // not permitted, OPEN
        CircuitBreakerUtils.printStatusInfo(circuitBreaker);

        // then
        Assertions.assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(4);
        Assertions.assertThat(metrics.getNumberOfSlowSuccessfulCalls()).isEqualTo(2);
        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }
}
