package com.sunset.spring.resilence4j.springboot2.client;

import com.sunset.spring.resilence4j.springboot2.Resilience4jTestApplication;
import com.sunset.spring.resilience4j.springboot2.internal.circuitbreaker.CircuitBreakerRegister;
import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteCallService;
import com.sunset.spring.resilience4j.springboot2.internal.client.RemoteClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {Resilience4jTestApplication.class})
public class RemoteClientCircuitBreakerTest {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RemoteClient remoteClient;
    @MockBean
    private RemoteCallService remoteCallService;

    @Before
    public void initStubbing() {
        // given
        when(remoteCallService.doSuccess()).thenReturn("OK");
        when(remoteCallService.doIgnoreException()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(remoteCallService.doException()).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        when(remoteCallService.doLatency()).thenAnswer(invocation -> {
            try {
                Thread.sleep(200);
            } catch (Exception ignored) {}
            return "LATENCY OK";
        });
    }

    @Test
    public void openStatusFallbackTest() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(CircuitBreakerRegister.REMOTE_CIRCUIT_BREAKER_NAME);

        // when
        remoteClient.doException();
        remoteClient.doException();

        // then
        Assertions.assertThat(circuitBreaker.getMetrics().getNumberOfFailedCalls()).isEqualTo(2);
        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }
}
