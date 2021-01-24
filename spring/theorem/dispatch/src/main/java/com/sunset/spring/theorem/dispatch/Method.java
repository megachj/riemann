package com.sunset.spring.theorem.dispatch;

import java.util.Arrays;
import java.util.List;

public class Method {
    public static void main(String[] args) {
        Method method = new Method();
        System.out.println(method.hello(Integer.valueOf(1)));
        System.out.println(method.hello(1));
        System.out.println(method.hello(1L));

        method.applyMyInteface(method::hello);
    }

    @FunctionalInterface
    interface MyInterface {
        String service(Integer id) throws Exception;
    }

    void applyMyInteface(MyInterface myInterface) {
        try {
            System.out.println(myInterface.service(1));
        } catch (Exception ignored) {}
    }

    // Method Signature: (method name, parameter list)
    // 메소드 정의할 때 적용
    String hello(Integer id) {
        return "hello1";
    }

    // 아래 메소드는 위 메소드와 시그니처가 같아서 정의될 수 없다.
    /*
    int hello(Integer s) {
        return 1;
    }
    */

    // 메소드 시그니처가 같아서 정의가 가능하다.
    String hello(int id) {
        return "hello2";
    }

    // Method Type: (return type, method type parameter, method argument type list, exception list)
    // Method Reference 를 사용할 때 적용
    <T extends Number> String hello(T id) throws IllegalArgumentException {
        return "hello3";
    }
}
