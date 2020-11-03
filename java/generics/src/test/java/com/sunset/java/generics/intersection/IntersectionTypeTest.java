package com.sunset.java.generics.intersection;

import org.junit.Test;

import java.util.function.Function;

import static com.sunset.java.generics.intersection.IntersectionType.*;

public class IntersectionTypeTest {

    /**
     * marker interface 테스트
     */
    @Test
    public void usage1_markerInterface() {
        printFuncResult(x -> 100, 10);
        printFuncResult((Function<Integer, Integer> & LinearFuncMarker) x -> 2 * x + 1, 10); // (Function<Integer, Integer> & LinearFuncMarker) is a Function<Integer, Integer>

        // printLinearFunctionResult((Function<Integer, Integer>)x -> 100, 10); // compile error: Function<Integer, Integer> is not a (Function<Integer, Integer> & LinearFuncMarker)
        printLinearFuncResult((Function<Integer, Integer> & LinearFuncMarker) x -> 2 * x + 1, 10);
    }


    /**
     * delegate 용법 테스트
     */
    @Test
    public void usage2_delegateModel() {
        run(()->"Sunset Choe", o -> {

        });
    }

    @Test
    public void usage3_() {

    }
}
