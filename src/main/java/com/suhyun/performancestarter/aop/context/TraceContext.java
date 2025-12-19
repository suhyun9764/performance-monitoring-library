package com.suhyun.performancestarter.aop.context;

import java.util.UUID;

/**
 * 같은 요청의 모든 메서드를 추적하기 위한 컨텍스트
 */
public class TraceContext {
    
    private static final ThreadLocal<String> traceId = new ThreadLocal<>();
    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);
    
    /**
     * 새로운 요청 시작
     */
    public static String startTrace() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        traceId.set(id);
        depth.set(0);
        return id;
    }
    
    /**
     * 현재 Trace ID 가져오기
     */
    public static String getCurrentTraceId() {
        String id = traceId.get();
        if (id == null) {
            id = startTrace();
        }
        return id;
    }
    
    /**
     * 깊이 증가 (메서드 진입)
     */
    public static int increaseDepth() {
        int current = depth.get();
        depth.set(current + 1);
        return current;
    }
    
    /**
     * 깊이 감소 (메서드 종료)
     */
    public static void decreaseDepth() {
        int current = depth.get();
        if (current > 0) {
            depth.set(current - 1);
        }
    }
    
    /**
     * 현재 깊이
     */
    public static int getCurrentDepth() {
        return depth.get();
    }
    
    /**
     * Trace 종료
     */
    public static void endTrace() {
        traceId.remove();
        depth.remove();
    }
}