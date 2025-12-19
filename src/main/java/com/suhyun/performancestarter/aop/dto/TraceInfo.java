package com.suhyun.performancestarter.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TraceInfo {
    private String traceId;
    private int depth;
    private boolean isRootCall;
}
