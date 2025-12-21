package com.suhyun.performancestarter.test.controller;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.test.service.TestService;
import com.suhyun.performancestarter.test.service.TestService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PerformanceMonitoring
public class TestController {
    private final TestService testService;
    private final TestService2 testService2;

    @GetMapping("/api/test")
    public void test() throws InterruptedException {
        testService.test();
        testService2.test();
    }
}
