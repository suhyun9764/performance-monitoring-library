package com.suhyun.performancestarter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MetricsDto {
    private int depth;
    private String layer;
    private String className;
    private String methodName;
    private String executionId;
    private String url;
    private long executionTime;
    private LocalDateTime timestamp;
    private int totalQueryCount;
    private String calledBy;
    private boolean hasNPlusOne;
    @Builder.Default
    private final List<NPlusOneIssue> nPlusOneIssues = new ArrayList<>();
}
