package com.sunset.spring.theorem.dispatch;

import java.util.Arrays;
import java.util.List;

public class Dispatch {
    public static void main(String[] args) {
        // Static Dispatch: Service 클래스의 run() 메소드가 호출될 지 컴파일 시점에 결정
        MyService1 svc1 = new MyService1();
        svc1.run(); // MyService1 의 run() 메소드가 호출되는게 컴파일 시점에 결정
        MyService2 svc2 = new MyService2();
        svc2.run(); // MyService2 의 run() 메소드가 호출되는게 컴파일 시점에 결정

        // Dynamic Dispatch: 메소드가 호출되는 런타임 시점에 할당되어 있는 오브젝트를 보고 결정
        Service svc = new MyService1();
        /*
        어떻게 런타임 시점에 MyService1 의 run() 메소드인지 알 수 있는걸까?
        자바 스펙에 보면 receiver parameter 라는것이 항상 메소드에 파라미터에 들어가게 된다.
        receiver parameter 란 생성된 오브젝트의 this 이다.
        따라서 receiver parameter 는 위의 MyService1 오브젝트의 this 이므로, 해당 run() 메소드를 실행할 수 있는 것이다.
         */
        svc.run();

        List<Service> svcList = Arrays.asList(new MyService1(), new MyService2());
        svcList.forEach(Service::run);
    }

    static abstract class Service {
        abstract void run();
    }

    static class MyService1 extends Service {
        @Override
        void run() {
            System.out.println("run1");
        }
    }

    static class MyService2 extends Service {
        @Override
        void run() {
            System.out.println("run2");
        }
    }
}
