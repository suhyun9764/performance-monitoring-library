package com.suhyun.performancestarter.model;

import lombok.*;

import java.time.LocalDateTime;

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
    @Setter
    private Long selfExecutionTime;
    private LocalDateTime timestamp;
    private final int totalQueryCount;
    private final boolean hasNPlusOne;
    private final String nPlusOnePattern;
    private final Integer nPlusOneCount;
}