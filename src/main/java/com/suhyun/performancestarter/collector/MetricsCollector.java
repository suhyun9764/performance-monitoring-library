package com.suhyun.performancestarter.collector;

import com.suhyun.performancestarter.model.PerformanceMetric;
import com.suhyun.performancestarter.model.RequestPerMetrics;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class MetricsCollector {

    private final Map<String, RequestPerMetrics> metricsByRequest = new ConcurrentHashMap<>();
    private final Map<String, List<PerformanceMetric>> metricsByMethod = new ConcurrentHashMap<>();

    public void save(PerformanceMetric metric) {
        String traceId = metric.getTraceId();
        if(!metricsByRequest.containsKey(traceId)) metricsByRequest.put(traceId,new RequestPerMetrics(traceId));
        metricsByRequest.get(traceId).addMetric(metric);

        String key = metric.getClassName() + "." + metric.getMethodName();
        metricsByMethod.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(metric);
    }

    public List<RequestPerMetrics> getAll() {
        return metricsByRequest.values().stream().toList();
    }

    /**
     * 최근 N개 메트릭 조회
     */
//    public List<PerformanceMetric> getRecent(int count) {
//        int size = metrics.size();
//        if (size == 0) {
//            return Collections.emptyList();
//        }
//        int fromIndex = Math.max(0, size - count);
//        return new ArrayList<>(metrics.subList(fromIndex, size));
//    }
//
//    /**
//     * 특정 메서드의 메트릭 조회
//     */
//    public List<PerformanceMetric> getByMethod(String className, String methodName) {
//        String key = className + "." + methodName;
//        return new ArrayList<>(metricsByMethod.getOrDefault(key, Collections.emptyList()));
//    }
//
//    /**
//     * 계층별 메트릭 조회
//     */
//    public List<PerformanceMetric> getByLayer(String layer) {
//        return metrics.stream()
//                .filter(m -> layer.equals(m.getLayer()))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 계층별 평균 실행 시간
//     */
//    public Map<String, Double> getAverageByLayer() {
//        if (metrics.isEmpty()) {
//            return Map.of(
//                    "CONTROLLER", 0.0,
//                    "SERVICE", 0.0,
//                    "REPOSITORY", 0.0
//            );
//        }
//
//        Map<String, Double> averages = metrics.stream()
//                .collect(Collectors.groupingBy(
//                        PerformanceMetric::getLayer,
//                        Collectors.averagingLong(PerformanceMetric::getExecutionTime)
//                ));
//
//        // 없는 계층은 0으로
//        averages.putIfAbsent("CONTROLLER", 0.0);
//        averages.putIfAbsent("SERVICE", 0.0);
//        averages.putIfAbsent("REPOSITORY", 0.0);
//
//        return averages;
//    }
//
//    /**
//     * 메서드별 평균 실행 시간
//     */
//    public Map<String, Double> getAverageByMethod() {
//        if (metrics.isEmpty()) {
//            return Collections.emptyMap();
//        }
//
//        return metrics.stream()
//                .collect(Collectors.groupingBy(
//                        m -> m.getClassName() + "." + m.getMethodName(),
//                        Collectors.averagingLong(PerformanceMetric::getExecutionTime)
//                ));
//    }
//
//    /**
//     * 가장 느린 메서드 Top N
//     */
//    public List<PerformanceMetric> getSlowestMethods(int count) {
//        return metrics.stream()
//                .sorted(Comparator.comparingLong(PerformanceMetric::getExecutionTime).reversed())
//                .limit(count)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 전체 메트릭 개수
//     */
//    public int getCount() {
//        return metrics.size();
//    }
//
    /**
     * 모든 메트릭 초기화
     */
    public void clear() {
        metricsByRequest.clear();
        metricsByMethod.clear();
    }

    public void calculateSelfExecutionTimes(String traceId) {
        metricsByRequest.get(traceId).calculateSelfExecutionTimes();
    }
}