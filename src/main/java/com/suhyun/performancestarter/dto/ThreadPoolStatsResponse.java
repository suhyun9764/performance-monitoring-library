package com.suhyun.performancestarter.dto;

import com.suhyun.performancestarter.model.AsyncThreadPoolStats;
import com.suhyun.performancestarter.model.TomcatThreadPoolStats;
import lombok.Getter;

/**
 * 모든 스레드 풀 통계를 담는 응답 DTO
 */
@Getter
public class ThreadPoolStatsResponse {
    
    private final TomcatStats tomcat;
    
    public ThreadPoolStatsResponse(TomcatThreadPoolStats tomcatStats) {
        this.tomcat = new TomcatStats(tomcatStats);
    }
    
    public TomcatStats getTomcat() { return tomcat; }
    
    /**
     * Tomcat 스레드 풀
     */
    public static class TomcatStats {
        private final int activeThreads;
        private final int currentThreads;
        private final int maxThreads;
        private final long connectionCount;
        private final int maxConnections;
        private final double threadUtilization;
        private final double threadActiveRate;
        private final double connectionUtilization;
        
        public TomcatStats(TomcatThreadPoolStats stats) {
            this.activeThreads = stats.getActiveThreads();
            this.currentThreads = stats.getCurrentThreads();
            this.maxThreads = stats.getMaxThreads();
            this.connectionCount = stats.getConnectionCount();
            this.maxConnections = stats.getMaxConnections();
            this.threadUtilization = stats.getThreadUtilization();
            this.threadActiveRate = stats.getThreadActiveRate();
            this.connectionUtilization = stats.getConnectionUtilization();
        }
    }
}