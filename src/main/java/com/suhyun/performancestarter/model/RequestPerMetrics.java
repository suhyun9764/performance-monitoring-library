package com.suhyun.performancestarter.model;

import com.suhyun.performancestarter.dto.MetricsDto;
import com.suhyun.performancestarter.dto.PerformanceMetric;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class RequestPerMetrics {
    private String traceId;
    private String requestUrl = null;
    private List<PerformanceMetric> metrics = new CopyOnWriteArrayList<>();
    private Long totalExecutionTime = 0L;

    public RequestPerMetrics(String traceId) {
        this.traceId = traceId;
    }

    public void addMetric(PerformanceMetric metric) {
        this.metrics.add(metric);
        if(requestUrl==null&&metric.getUrl()!=null) requestUrl = metric.getUrl();

    }

    public void calculate(){

        if (metrics.isEmpty()) {
            return;
        }

        Map<String, PerformanceMetric> metricMap = new HashMap<>();
        for (PerformanceMetric metric : metrics) {
            metricMap.put(metric.getExecutionId(), metric);
        }

        Map<String, List<PerformanceMetric>> childrenMap = new HashMap<>();
        for (PerformanceMetric metric : metrics) {
            String parentId = metric.getCalledBy();
            if (parentId != null) {
                childrenMap
                        .computeIfAbsent(parentId, k -> new ArrayList<>())
                        .add(metric);
            }
        }

        for (PerformanceMetric metric : metrics) {
            List<PerformanceMetric> children =
                    childrenMap.getOrDefault(metric.getExecutionId(), Collections.emptyList());

            long childrenTimeSum = children.stream()
                    .mapToLong(PerformanceMetric::getExecutionTime)
                    .sum();

            long selfTime = metric.getExecutionTime() - childrenTimeSum;

            metric.setSelfExecutionTime(Math.max(0, selfTime));
            this.totalExecutionTime += metric.getSelfExecutionTime();
        }
    }
}
