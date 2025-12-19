package com.suhyun.performancestarter.service;

import com.suhyun.performancestarter.model.AsyncThreadPoolStats;
import org.springframework.stereotype.Component;

/**
 * 비동기 스레드 풀 모니터링
 */
@Component
public class AsyncThreadPoolMonitor {
    
    private final AsyncExecutor asyncExecutor;
    
    public AsyncThreadPoolMonitor(AsyncExecutor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }
    
    /**
     * 비동기 스레드 풀 상태 조회
     */
    public AsyncThreadPoolStats getStats() {
        return asyncExecutor.getStats();
    }


}