package com.suhyun.performancestarter.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {
    private String methodName;
    private String className;
    private String url;
    private String layer;
    private boolean queryMonitoring;

}
