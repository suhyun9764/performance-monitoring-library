package com.suhyun.performancestarter.test.repository;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import org.springframework.stereotype.Repository;

@PerformanceMonitoring
@Repository
public class TestRepository2 {
    public void test() throws InterruptedException {
        Thread.sleep(100);
    }
}
