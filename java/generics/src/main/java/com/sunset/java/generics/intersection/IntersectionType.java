package com.sunset.java.generics.intersection;

import java.util.function.Consumer;
import java.util.function.Function;

public class IntersectionType {

    /**
     * marker interface: 구현 메소드가 0개인 인터페이스
     *
     * Serializable, Cloneable 등
     */
    public interface LinearFuncMarker {}

    public static void printFuncResult(Function<Integer, Integer> fx, Integer a) {
        System.out.println("func result: " + fx.apply(a));
    }

    public static <T extends Function<Integer, Integer> & LinearFuncMarker> void printLinearFuncResult(T fx, Integer a) {
        System.out.println("linear func result: " + fx.apply(a));
    }

    /**
     *
     *
     * @param <T>
     */
    public interface DelegateTo<T> {
        T delegate();
    }

    public interface Bot extends DelegateTo<String> {
        default void hello() {
            System.out.println("Hello " + delegate());
        }

        default void goodbye() {
            System.out.println("Goodbye " + delegate());
        }
    }

    public static <T extends DelegateTo<S>, S> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }
}
