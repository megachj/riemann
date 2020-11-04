package com.sunset.java.generics;

import org.junit.Test;

import java.util.function.Function;

import static com.sunset.java.generics.IntersectionType.*;

public class IntersectionTypeTest {

    /**
     * marker interface
     */
    @Test
    public void usage1_markerInterface() {
        printFuncResult(x -> 100, 1);
        printFuncResult((Function<Integer, Integer> & LinearFuncMarker) x -> 2 * x + 1, 1); // (Function<Integer, Integer> & LinearFuncMarker) is a Function<Integer, Integer>

        // printLinearFuncResult((Function<Integer, Integer>)x -> 100, 10); // compile error: Function<Integer, Integer> is not a (Function<Integer, Integer> & LinearFuncMarker)
        printLinearFuncResult((Function<Integer, Integer> & LinearFuncMarker) x -> 2 * x + 1, 1);
    }

    /**
     * interface default method + callback
     */
    @Test
    public void usage2_defaultMethodCallback() {
        int a = 3;
        int r = 4;

        // fx 는 함수(FunctionalInterface 객체). x -> 2x + 1
        // fx.apply(a) 2a + 1
        run((Function<Integer, Integer>)x -> 2 * x + 1, fx -> {
            System.out.println("result: " + fx.apply(a));
            // fx.opposite(a); // compile error
        });

        run((LibraryModel & CustomModel) x -> 2 * x + 1, fx -> {
            System.out.println("result: " + fx.apply(a));
            System.out.println("opposite result: " + fx.opposite(a));
            System.out.println("remains result: " + fx.remains(a, 4));
        });

        int result = run((LibraryModel & CustomModel)x -> x * x,
                fx -> fx.apply(a) + fx.opposite(a) + fx.remains(a, r));
        System.out.println("x^2 remains result: " + result);
    }
}
