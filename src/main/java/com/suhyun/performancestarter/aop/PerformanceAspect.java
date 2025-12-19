package com.suhyun.performancestarter.aop;

import com.suhyun.performancestarter.aop.enums.Layer;
import com.suhyun.performancestarter.aop.utils.MethodInfoExtractor;
import com.suhyun.performancestarter.aop.utils.QueryCounter;
import com.suhyun.performancestarter.aop.utils.TraceManager;
import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.dto.QueryInfo;
import com.suhyun.performancestarter.model.QueryStatistics;
import com.suhyun.performancestarter.service.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceAspect {
    private final TraceManager traceManager;
    private final MethodInfoExtractor methodInfoExtractor;
    private final MetricsService metricsService;
    private final QueryCounter queryCounter;

    @Around("@annotation(com.suhyun.performancestarter.annotation.PerformanceMonitoring)")
    public Object measureMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[PERF] {} 실행 시작", joinPoint.getSignature());
        return measurePerformance(joinPoint);
    }

    @Around("@within(com.suhyun.performancestarter.annotation.PerformanceMonitoring)")
    public Object measureClass(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[PERF] {} 실행 시작", joinPoint.getSignature());
        return measurePerformance(joinPoint);
    }

//    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
//    public Object measureAllRepositories(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("🟡 [ALL REPO] " + joinPoint.getSignature());
//        return measurePerformance(joinPoint);
//    }


    private Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceInfo traceInfo = traceManager.start();
        MethodInfo methodInfo = methodInfoExtractor.extract(joinPoint);

//        boolean isService = methodInfo.getLayer().equals(Layer.SERVICE.name());
//            if(isService){
//                queryCounter.startCounting();
//            }
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000;
        QueryInfo queryInfo = null;
//        if (isService) {
//            QueryStatistics stats = queryCounter.getStatistics();
//            if (stats.getTotalQueryCount() > 0) {
//                queryInfo = buildQueryInfo(stats, methodInfo);
//            }
//            queryCounter.clear();
//        }
        metricsService.saveAsync(traceInfo, methodInfo, executionTime,queryInfo);
        traceManager.end(traceInfo);

        return result;
    }

    private QueryInfo buildQueryInfo(QueryStatistics stats, MethodInfo methodInfo) {
        QueryInfo.QueryInfoBuilder builder = QueryInfo.builder()
                .totalQueryCount(stats.getTotalQueryCount())
                .hasNPlusOne(stats.hasNPlusOne());

        if (stats.hasNPlusOne()) {
            Map.Entry<String, Integer> worst = stats.getMostFrequentPattern();

            System.out.println("\n⚠️  N+1 쿼리 감지!");
            System.out.println("위치: " + methodInfo.getClassName() + "." + methodInfo.getMethodName() + "()");
            System.out.println("총 쿼리: " + stats.getTotalQueryCount() + "개");
            System.out.println("반복 패턴: " + worst.getKey() + " (" + worst.getValue() + "회)");
            System.out.println("💡 해결: @EntityGraph 또는 Fetch Join 사용\n");

            builder.nPlusOnePattern(worst.getKey())
                    .nPlusOneCount(worst.getValue());
        }

        return builder.build();
    }

}
