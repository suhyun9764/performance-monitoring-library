package com.suhyun.performancestarter.aop.repository;

import com.suhyun.performancestarter.model.QueryStatistics;

import java.util.HashMap;
import java.util.Map;

public class QueryRepository {
    private static final ThreadLocal<Map<String, QueryStatistics>> statisticsById =
            ThreadLocal.withInitial(HashMap::new);

    private static final ThreadLocal<java.util.Deque<String>> activeMethodIds =
            ThreadLocal.withInitial(java.util.ArrayDeque::new);

    public static void activateMonitoring(String methodId) {
        activeMethodIds.get().push(methodId);
    }

    public static void addQuery(String sql) {
        java.util.Deque<String> stack = activeMethodIds.get();

        for (String methodId : stack) {
            Map<String, QueryStatistics> map = statisticsById.get();
            QueryStatistics stats = map.computeIfAbsent(methodId, k -> new QueryStatistics());
            stats.addQuery(sql);
        }
    }

    public static void deactivateMonitoring() {
        java.util.Deque<String> stack = activeMethodIds.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }

    public static QueryStatistics getStatistics(String methodId) {
        Map<String, QueryStatistics> map = statisticsById.get();
        return map.getOrDefault(methodId, new QueryStatistics());
    }

    public static void clearAll() {
        statisticsById.remove();
        activeMethodIds.remove();
    }
}
