package com.suhyun.performancestarter.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class PerformanceMetric {
    private String traceId;
    private int depth;
    private String layer;
    private String className;
    private String methodName;
    private String url;
    private long executionTime;
    private LocalDateTime timestamp;
    private final int totalQueryCount;
    private final boolean hasNPlusOne;
    @Builder.Default
    private final List<NPlusOneIssue> nPlusOneIssues = new ArrayList<>();
}