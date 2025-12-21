package com.suhyun.performancestarter.collector;

import com.suhyun.performancestarter.model.RequestPerMetrics;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MetricsRepository {
    private final Map<String, MetricsInfo> metricsByRequest = new ConcurrentHashMap<>();
}
