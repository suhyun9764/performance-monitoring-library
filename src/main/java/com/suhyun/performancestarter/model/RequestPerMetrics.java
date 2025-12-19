package com.suhyun.performancestarter.model;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class RequestPerMetrics {
    private String traceId;
    private String requestUrl = null;
    private List<PerformanceMetric> metrics = new CopyOnWriteArrayList<>();

    public RequestPerMetrics(String traceId) {
        this.traceId = traceId;
    }

    public void addMetric(PerformanceMetric metric) {
        this.metrics.add(metric);
        if(requestUrl==null&&metric.getUrl()!=null) requestUrl = metric.getUrl();

    }

    public Long getTotalExecutionTime(){
        return metrics.stream()
                .filter(m->m.getDepth()==0)
                .findFirst()
                .map(PerformanceMetric::getExecutionTime)
                .orElse(0L);
    }

    public int getCount(){
        return metrics.size();
    }

    public void calculateSelfExecutionTimes() {
        Map<Integer, Long> childrenTimeByDepth = new HashMap<>();

        List<PerformanceMetric> sorted = new ArrayList<>(metrics);
        sorted.sort((a, b) -> b.getDepth() - a.getDepth());

        for (PerformanceMetric metric : sorted) {
            int depth = metric.getDepth();
            long totalTime = metric.getExecutionTime();
            long childrenTime = childrenTimeByDepth.getOrDefault(depth + 1, 0L);
            long selfTime = totalTime - childrenTime;
            metric.setSelfExecutionTime(selfTime);
            childrenTimeByDepth.merge(depth, totalTime, Long::sum);
        }
    }
}
