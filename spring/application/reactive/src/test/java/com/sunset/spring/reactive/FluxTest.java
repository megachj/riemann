package com.sunset.spring.reactive;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

public class FluxTest {

    @Test
    public void throwException() {
        Flux<Integer> integerFlux = Flux.fromStream(IntStream.rangeClosed(1, 10).boxed())
                .map(i -> {
                    verifyRemains(i, 3);
                    return i;
                });
        
        integerFlux.subscribe();
    }

    @Test
    public void doOnError() {
        Flux<Integer> integerFlux = Flux.fromStream(IntStream.rangeClosed(1, 10).boxed())
                .map(i -> {
                    verifyRemains(i, 3);
                    return i;
                })
                .doOnError(ArithmeticException.class, e -> {
                    System.out.printf("%s, arithmetic exception 검출\n", Thread.currentThread());
                    throw new CustomException();
                })
                .map(i -> {
                    System.out.printf("%s, result: %d\n", Thread.currentThread(), i);
                    return i;
                });
        integerFlux.subscribe();
        try {
            integerFlux.blockFirst();
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void onErrorMap() {
        Flux<Integer> integerFlux = Flux.fromStream(IntStream.rangeClosed(1, 10).boxed())
                .map(i -> {
                    verifyRemains(i, 3);
                    return i;
                })
                .onErrorMap(ArithmeticException.class, e -> {
                    System.out.printf("%s, arithmetic exception 검출\n", Thread.currentThread());
                    throw new CustomException();
                })
                .map(i -> {
                    System.out.printf("%s, result: %d\n", Thread.currentThread(), i);
                    return i;
                });
        integerFlux.subscribe();
        integerFlux.blockFirst();
    }

    @Test
    public void onErrorContinue() {
        Flux<Integer> integerFlux = Flux.fromStream(IntStream.rangeClosed(1, 10).boxed())
                .map(i -> {
                    verifyRemains(i, 3);
                    return i;
                })
                .onErrorContinue(ArithmeticException.class, (throwable, o) -> {
                    System.out.printf("%s, arithmetic exception 검출\n", Thread.currentThread());
                })
                .map(i -> {
                    System.out.printf("%s, result: %d\n", Thread.currentThread(), i);
                    return i;
                });
        integerFlux.subscribe();
    }

    private void verifyRemains(int i, int r) {
        if (i % r == 0)
            throw new ArithmeticException();
    }

    public static class CustomException extends RuntimeException {}
}
