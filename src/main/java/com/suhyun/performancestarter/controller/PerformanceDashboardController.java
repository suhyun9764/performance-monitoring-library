package com.suhyun.performancestarter.controller;

import com.suhyun.performancestarter.model.RequestPerMetrics;
import com.suhyun.performancestarter.model.TomcatThreadPoolStats;
import com.suhyun.performancestarter.monitor.TomcatThreadPoolMonitor;
import com.suhyun.performancestarter.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
public class PerformanceDashboardController {

    private final MetricsService metricsService;
    private final TomcatThreadPoolMonitor tomcatMonitor;

    @GetMapping("/api/metrics")
    public List<RequestPerMetrics> getAllMetrics() {
        return metricsService.getAll();
    }

    @GetMapping("/api/clear")
    public Map<String, String> clearMetrics() {
        return metricsService.clear();

    }

    @GetMapping("/api/thread-pool")
    public TomcatThreadPoolStats getThreadPoolStats() {
        return tomcatMonitor.getStats();

    }
}