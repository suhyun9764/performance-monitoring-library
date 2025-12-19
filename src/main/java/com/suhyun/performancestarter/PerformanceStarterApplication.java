package com.suhyun.performancestarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // 테스트를 위해 필요!
public class PerformanceStarterApplication {
    public static void main(String[] args) {
        // 실행할 필요는 없지만 테스트를 위해 클래스는 있어야 함
        SpringApplication.run(PerformanceStarterApplication.class, args);
    }
}