package com.suhyun.performancestarter.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AsyncThreadPoolStats {
    private final int activeThreads;      // 현재 실행 중
    private final int currentPoolSize;    // 현재 풀 크기
    private final int corePoolSize;       // 코어 크기
    private final int maxPoolSize;        // 최대 크기
    private final int queueSize;          // 대기 큐 크기
    private final int queueCapacity;      // 큐 최대 용량
    private final long completedTasks;    // 완료된 작업
    private final long totalTasks;        // 총 작업


    /**
     * 스레드 사용률 (%)
     */
    public double getThreadUtilization() {
        return maxPoolSize > 0 ? (currentPoolSize * 100.0 / maxPoolSize) : 0;
    }

    /**
     * 큐 사용률 (%)
     */
    public double getQueueUtilization() {
        return queueCapacity > 0 ? (queueSize * 100.0 / queueCapacity) : 0;
    }

    /**
     * 작업 처리율 (%)
     */
    public double getCompletionRate() {
        return totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 100;
    }

    /**
     * 스레드 활성률 (%) - 실제 일하는 스레드 비율
     */
    public double getThreadActiveRate() {
        return currentPoolSize > 0 ? (activeThreads * 100.0 / currentPoolSize) : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "PoolStats{active=%d/%d, pool=%d/%d (%.1f%%), queue=%d/%d (%.1f%%), completed=%d/%d (%.1f%%)}",
                activeThreads, currentPoolSize,
                currentPoolSize, maxPoolSize, getThreadUtilization(),
                queueSize, queueCapacity, getQueueUtilization(),
                completedTasks, totalTasks, getCompletionRate()
        );
    }
}
