package com.suhyun.performancestarter.service;

import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.dto.QueryInfo;
import com.suhyun.performancestarter.model.RequestPerMetrics;

import java.util.List;
import java.util.Map;

public interface MetricsService {
    void saveAsync(TraceInfo traceInfo, MethodInfo methodInfo, Long executionTime, QueryInfo queryInfo);

    List<RequestPerMetrics> getAll();

    Map<String, String> clear();
}
