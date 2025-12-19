package com.suhyun.performancestarter.test.service;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.test.repository.TestRepository;
import com.suhyun.performancestarter.test.repository.TestRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@PerformanceMonitoring
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TestRepository2 testRepository2;
    @Override
    public void test() throws InterruptedException {
        testRepository.findAll();
        testRepository2.test();
    }

}
