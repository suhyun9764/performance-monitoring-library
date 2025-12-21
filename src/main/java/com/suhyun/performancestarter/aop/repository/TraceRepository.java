package com.suhyun.performancestarter.aop.repository;

import java.util.UUID;

public class TraceRepository {
    
    private static final ThreadLocal<String> traceId = new ThreadLocal<>();
    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    private static String startTrace() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        traceId.set(id);
        depth.set(0);
        return id;
    }

    public static String getCurrentTraceId() {
        String id = traceId.get();
        if (id == null) {
            id = startTrace();
        }
        return id;
    }

    public static int increaseDepth() {
        int current = depth.get();
        depth.set(current + 1);
        return current;
    }

    public static void decreaseDepth() {
        int current = depth.get();
        if (current > 0) {
            depth.set(current - 1);
        }
    }

    public static int getCurrentDepth() {
        return depth.get();
    }

    public static void endTrace() {
        traceId.remove();
        depth.remove();
    }
}