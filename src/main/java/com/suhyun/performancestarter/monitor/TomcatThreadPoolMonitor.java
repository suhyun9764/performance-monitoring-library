package com.suhyun.performancestarter.monitor;

import com.suhyun.performancestarter.model.TomcatThreadPoolStats;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Tomcat 스레드 풀 모니터링
 */
@Component
public class TomcatThreadPoolMonitor {

    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public TomcatThreadPoolStats getStats() {
        // 매번 새로 조회
        int threadCount = threadMXBean.getThreadCount();
        long[] threadIds = threadMXBean.getAllThreadIds();

        int httpThreads = 0;
        int runnableThreads = 0;
        int waitingThreads = 0;
        int timedWaitingThreads = 0;


        for (long threadId : threadIds) {
            ThreadInfo info = threadMXBean.getThreadInfo(threadId);
            if (info == null) continue;

            String name = info.getThreadName();
            Thread.State state = info.getThreadState();

            // HTTP 처리 스레드 카운트
            if (isHttpThread(name)) {
                httpThreads++;

                // 상태별 카운트
                if (state == Thread.State.RUNNABLE) {
                    runnableThreads++;
                } else if (state == Thread.State.WAITING) {
                    waitingThreads++;
                } else if (state == Thread.State.TIMED_WAITING) {
                    timedWaitingThreads++;
                }
            }
        }

        if (httpThreads == 0) {
            httpThreads = threadCount;

            for (long threadId : threadIds) {
                ThreadInfo info = threadMXBean.getThreadInfo(threadId);
                if (info == null) continue;

                if (info.getThreadState() == Thread.State.RUNNABLE) {
                    runnableThreads++;
                }
            }
        }
        return new TomcatThreadPoolStats(
                runnableThreads,     // 활성 스레드
                httpThreads,         // 현재 스레드
                200,                 // 최대 스레드
                runnableThreads,     // 연결 수
                8192                 // 최대 연결
        );
    }

    private boolean isHttpThread(String threadName) {
        String lower = threadName.toLowerCase();
        return lower.contains("http") ||
                lower.contains("nio") ||
                lower.contains("exec") ||
                lower.contains("tomcat") ||
                lower.contains("worker") ||
                (lower.contains("thread") && lower.contains("-"));  // thread-1, thread-2 등
    }
}