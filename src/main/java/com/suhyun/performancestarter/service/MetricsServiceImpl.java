package com.suhyun.performancestarter.service;

import com.suhyun.performancestarter.collector.MetricsCollector;
import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.dto.QueryInfo;
import com.suhyun.performancestarter.model.PerformanceMetric;
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
    private final AsyncExecutor asyncExecutor;

    public void saveAsync(TraceInfo traceInfo, MethodInfo methodInfo, Long executionTime, QueryInfo queryInfo){
//        System.out.println("[MetricsService] saveAsync called on thread: " + Thread.currentThread().getName());

        asyncExecutor.executeAsync(() -> {
//            System.out.println("[MetricsService] save executing on thread: " + Thread.currentThread().getName());
            save(traceInfo, methodInfo, executionTime,queryInfo);
        });

//        System.out.println("[MetricsService] saveAsync returned immediately (non-blocking)");
    }

    private void save(TraceInfo traceInfo, MethodInfo methodInfo, Long executionTime, QueryInfo queryInfo) {
        PerformanceMetric metric = buildMetric(traceInfo, methodInfo, executionTime,queryInfo);

        metricsCollector.save(metric);

        if (traceInfo.isRootCall()) {
            metricsCollector.calculateSelfExecutionTimes(traceInfo.getTraceId());
        }

//        System.out.println("[MetricsService] save completed on thread: " + Thread.currentThread().getName());
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


    private PerformanceMetric buildMetric(TraceInfo traceInfo, MethodInfo methodInfo,
                                          long executionTime, QueryInfo queryInfo) {
        return PerformanceMetric.builder()
                .traceId(traceInfo.getTraceId())
                .depth(traceInfo.getDepth())
                .layer(methodInfo.getLayer())
                .className(methodInfo.getClassName())
                .methodName(methodInfo.getMethodName())
                .url(methodInfo.getUrl())
                .executionTime(executionTime)
                .timestamp(LocalDateTime.now())
                // 쿼리 정보 안전하게 추가
                .totalQueryCount(getQueryCount(queryInfo))
                .hasNPlusOne(hasNPlusOne(queryInfo))
                .nPlusOnePattern(getNPlusOnePattern(queryInfo))
                .nPlusOneCount(getNPlusOneCount(queryInfo))
                .build();
    }

    private int getQueryCount(QueryInfo queryInfo) {
        return queryInfo != null ? queryInfo.getTotalQueryCount() : 0;
    }

    private boolean hasNPlusOne(QueryInfo queryInfo) {
        return queryInfo != null && queryInfo.isHasNPlusOne();
    }

    private String getNPlusOnePattern(QueryInfo queryInfo) {
        return queryInfo != null ? queryInfo.getNPlusOnePattern() : null;
    }

    private Integer getNPlusOneCount(QueryInfo queryInfo) {
        return queryInfo != null ? queryInfo.getNPlusOneCount() : null;
    }

}
