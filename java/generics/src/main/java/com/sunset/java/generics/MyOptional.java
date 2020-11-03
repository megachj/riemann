package com.sunset.java.generics;

import java.lang.reflect.Type;

class MyOptional<T> {
    T t;

    private MyOptional(T t){
        this.t = t;
    }

    public static <T> MyOptional<T> ofNullable(T t){
        return new MyOptional<>(t);
    }

    public T get(){
        return this.t;
    }

    public Type getTypeName(){
        try {
            return this.getClass().getDeclaredField("t").getType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
