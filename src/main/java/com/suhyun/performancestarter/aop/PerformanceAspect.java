package com.suhyun.performancestarter.aop;

import com.suhyun.performancestarter.aop.manager.QueryManager;
import com.suhyun.performancestarter.aop.utils.MethodInfoExtractor;
import com.suhyun.performancestarter.aop.manager.TraceManager;
import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.dto.NPlusOneIssue;
import com.suhyun.performancestarter.aop.dto.QueryInfo;
import com.suhyun.performancestarter.model.QueryStatistics;
import com.suhyun.performancestarter.service.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceAspect {
    private final TraceManager traceManager;
    private final MethodInfoExtractor methodInfoExtractor;
    private final MetricsService metricsService;
    private final QueryManager queryManager;


    @Around("@annotation(com.suhyun.performancestarter.annotation.PerformanceMonitoring)")
    public Object measureMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        return measurePerformance(joinPoint);
    }

    @Around("@within(com.suhyun.performancestarter.annotation.PerformanceMonitoring)")
    public Object measureClass(ProceedingJoinPoint joinPoint) throws Throwable {
        return measurePerformance(joinPoint);
    }

    private Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceInfo traceInfo = traceManager.start();
        MethodInfo methodInfo = methodInfoExtractor.extract(joinPoint);
        if(methodInfo.isQueryMonitoring()){
            queryManager.activateMonitoring(traceInfo.getExecutionId());
        }

        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000;

        QueryInfo queryInfo = null;
        if (methodInfo.isQueryMonitoring()) {
            QueryStatistics stats = queryManager.getStatistics(traceInfo.getExecutionId());
            if (stats.getTotalQueryCount() > 0) {
                queryInfo = buildQueryInfo(stats);
            }
            queryManager.deactivateMonitoring();
        }

        metricsService.save(traceInfo, methodInfo, executionTime, queryInfo);
        traceManager.end();
        if(traceInfo.isRootCall())
            queryManager.end();
        return result;
    }

    private QueryInfo buildQueryInfo(QueryStatistics stats) {
        QueryInfo.QueryInfoBuilder builder = QueryInfo.builder()
                .totalQueryCount(stats.getTotalQueryCount())
                .hasNPlusOne(stats.hasNPlusOne());

        if (stats.hasNPlusOne()) {
            List<Map.Entry<String, Integer>> suspiciousPatterns =
                    stats.getSuspiciousPatterns();

            List<NPlusOneIssue> issues = suspiciousPatterns.stream()
                    .map(entry -> NPlusOneIssue.builder()
                            .pattern(entry.getKey())
                            .count(entry.getValue())
                            .build())
                    .toList();

            builder.nPlusOneIssues(issues);
        }

        return builder.build();
    }

}
