package com.suhyun.performancestarter.service;

import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.aop.dto.QueryInfo;
import com.suhyun.performancestarter.dto.PerformanceMetric;
import com.suhyun.performancestarter.model.RequestPerMetrics;

import java.util.List;
import java.util.Map;

public interface MetricsService {
    void saveAsync(PerformanceMetric metric);

    List<RequestPerMetrics> getAll();

    Map<String, String> clear();

    PerformanceMetric createMetric(TraceInfo traceInfo, MethodInfo methodInfo, long executionTime, QueryInfo queryInfo);
}
