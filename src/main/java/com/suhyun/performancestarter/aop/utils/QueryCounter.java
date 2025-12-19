package com.suhyun.performancestarter.aop.utils;

import com.suhyun.performancestarter.model.QueryStatistics;
import org.springframework.stereotype.Component;

@Component
public class QueryCounter {

    private final ThreadLocal<QueryStatistics> statistics =
            ThreadLocal.withInitial(QueryStatistics::new);

    public void startCounting() {
        statistics.get().reset();
    }

    public void incrementQuery(String sql) {
        statistics.get().addQuery(sql);
    }

    public QueryStatistics getStatistics() {
        return statistics.get();
    }

    public void clear() {
        statistics.remove();
    }
}