package com.suhyun.performancestarter.aop.repository;

import com.suhyun.performancestarter.aop.dto.TraceInfo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class TraceRepository {

    private static final ThreadLocal<String> traceId = new ThreadLocal<>();
    private static final ThreadLocal<Deque<String>> executionStack =
            ThreadLocal.withInitial(ArrayDeque::new);

    private static String startTrace() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        traceId.set(id);
        return id;
    }

    public static TraceInfo getTrace() {
        String id = traceId.get();
        boolean isRootCall = false;
        if (id == null) {
            id = startTrace();
            isRootCall = true;
        }
        String executionId = UUID.randomUUID().toString();
        String parentExecutionId = executionStack.get().peek();
        executionStack.get().push(executionId);
        return TraceInfo.builder()
                .traceId(id)
                .depth(executionStack.get().size()) // 필요하면)
                .isRootCall(isRootCall)
                .calledBy(parentExecutionId)
                .executionId(executionId)
                .build();
    }

//    public static int increaseDepth() {
//        int current = depth.get();
//        depth.set(current + 1);
//        return current;
//    }

//    public static void decreaseDepth() {
//        executionStack.get().pop();
//        if (executionStack.get().isEmpty()) {
//            traceId.remove();
//            executionStack.remove();
//        }
//    }

//    public static int getCurrentDepth() {
//        return depth.get();
//    }

    public static void endTrace() {
        executionStack.get().pop();
        if (executionStack.get().isEmpty()) {
            traceId.remove();
            executionStack.remove();
        }
    }
}