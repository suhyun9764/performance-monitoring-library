package com.suhyun.performancestarter.model;

import lombok.Getter;

@Getter
public class TomcatThreadPoolStats {
    private final int activeThreads;      // 활성 스레드 (처리 중)
    private final int currentThreads;     // 현재 스레드 수
    private final int maxThreads;         // 최대 스레드
    private final long connectionCount;   // 현재 연결 수
    private final int maxConnections;     // 최대 연결 수

    public TomcatThreadPoolStats(
            int activeThreads,
            int currentThreads,
            int maxThreads,
            long connectionCount,
            int maxConnections) {
        this.activeThreads = activeThreads;
        this.currentThreads = currentThreads;
        this.maxThreads = maxThreads;
        this.connectionCount = connectionCount;
        this.maxConnections = maxConnections;
    }

    public double getThreadUtilization() {
        return maxThreads > 0 ? (currentThreads * 100.0 / maxThreads) : 0;
    }

    /**
     * 스레드 활성률 (%)
     */
    public double getThreadActiveRate() {
        return currentThreads > 0 ? (activeThreads * 100.0 / currentThreads) : 0;
    }

    /**
     * 연결 사용률 (%)
     */
    public double getConnectionUtilization() {
        return maxConnections > 0 ? (connectionCount * 100.0 / maxConnections) : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "TomcatStats{active=%d/%d, threads=%d/%d (%.1f%%), connections=%d/%d (%.1f%%)}",
                activeThreads, currentThreads,
                currentThreads, maxThreads, getThreadUtilization(),
                connectionCount, maxConnections, getConnectionUtilization()
        );
    }
}
