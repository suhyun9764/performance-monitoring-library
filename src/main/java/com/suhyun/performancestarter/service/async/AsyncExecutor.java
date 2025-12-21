package com.suhyun.performancestarter.service.async;

import com.suhyun.performancestarter.model.AsyncThreadPoolStats;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 비동기 실행을 위한 스레드 풀 관리
 */
@Component
public class AsyncExecutor {

    @Value("${performance.monitoring.async.core-pool-size:2}")
    private int corePoolSize;

    @Value("${performance.monitoring.async.max-pool-size:4}")
    private int maxPoolSize;

    @Value("${performance.monitoring.async.queue-capacity:1000}")
    private int queueCapacity;

    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init() {
        this.executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new PerformanceThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void executeAsync(Runnable task) {


        CompletableFuture.runAsync(() -> {
            task.run();
        }, executor).exceptionally(throwable -> {
            return null;
        });
    }

    public AsyncThreadPoolStats getStats() {
        return new AsyncThreadPoolStats(
                executor.getActiveCount(),           // 현재 실행 중인 스레드 수
                executor.getPoolSize(),              // 현재 스레드 풀 크기
                executor.getCorePoolSize(),          // 코어 스레드 수
                executor.getMaximumPoolSize(),       // 최대 스레드 수
                executor.getQueue().size(),          // 대기 중인 작업 수
                queueCapacity,                       // 최대 큐 크기
                executor.getCompletedTaskCount(),    // 완료된 작업 수
                executor.getTaskCount()              // 총 작업 수 (완료 + 실행중 + 대기)
        );
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static class PerformanceThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("performance-metrics-" + (++counter));
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    }
}