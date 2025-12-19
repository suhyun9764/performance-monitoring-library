package com.suhyun.performancestarter.test.controller;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PerformanceMonitoring
public class TestController {
    private final TestService testService;

    @GetMapping("/api/test")
    public void test() throws InterruptedException {
        testService.test();
    }
}
