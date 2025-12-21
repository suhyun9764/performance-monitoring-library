package com.suhyun.performancestarter.model;

import com.suhyun.performancestarter.dto.PerformanceMetric;
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
}
