package com.suhyun.performancestarter.service;

import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.aop.dto.QueryInfo;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.collector.MetricsCollector;
import com.suhyun.performancestarter.dto.NPlusOneIssue;
import com.suhyun.performancestarter.dto.PerformanceMetric;
import com.suhyun.performancestarter.model.RequestPerMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {
    private final MetricsCollector metricsCollector;

    @Override
    public void save(TraceInfo traceInfo, MethodInfo methodInfo, long executionTime, QueryInfo queryInfo) {
            metricsCollector.save(buildMetric(traceInfo, methodInfo, executionTime, queryInfo));
    }

    @Override
    public List<RequestPerMetrics> getAll() {
        return metricsCollector.getAll();
    }

    @Override
    public Map<String, String> clear() {
        metricsCollector.clear();
        return Map.of("status", "cleared");
    }

    @Override
    public PerformanceMetric createMetric(TraceInfo traceInfo, MethodInfo methodInfo, long executionTime, QueryInfo queryInfo) {
        return buildMetric(traceInfo, methodInfo, executionTime, queryInfo);
    }


    private PerformanceMetric buildMetric(TraceInfo traceInfo, MethodInfo methodInfo,
                                          long executionTime, QueryInfo queryInfo) {
        return PerformanceMetric.builder()
                .executionId(traceInfo.getExecutionId())
                .traceId(traceInfo.getTraceId())
                .calledBy(traceInfo.getCalledBy())
                .depth(traceInfo.getDepth())
                .layer(methodInfo.getLayer())
                .className(methodInfo.getClassName())
                .methodName(methodInfo.getMethodName())
                .url(methodInfo.getUrl())
                .executionTime(executionTime)
                .timestamp(LocalDateTime.now())
                .totalQueryCount(getQueryCount(queryInfo))
                .hasNPlusOne(hasNPlusOne(queryInfo))
                .nPlusOneIssues(getNPlusOneIssues(queryInfo))
                .build();
    }

    private List<NPlusOneIssue> getNPlusOneIssues(QueryInfo queryInfo) {
        return queryInfo != null ? queryInfo.getNPlusOneIssues() : List.of();
    }

    private int getQueryCount(QueryInfo queryInfo) {
        return queryInfo != null ? queryInfo.getTotalQueryCount() : 0;
    }

    private boolean hasNPlusOne(QueryInfo queryInfo) {
        return queryInfo != null && queryInfo.isHasNPlusOne();
    }


}
