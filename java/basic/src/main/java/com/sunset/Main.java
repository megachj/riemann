package com.sunset;

import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        // resources(여기선 data 디렉토리도 포함) 에 있는 데이터 불러오기
        URL resource = Main.class.getClassLoader().getResource("input/dummy-bank-account.json");
        System.out.println("resources" + resource.getPath());

        
    }
}
