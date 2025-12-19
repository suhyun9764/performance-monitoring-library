package com.suhyun.performancestarter.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {
    /*
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = signature.getMethod().getName();
        Class<?> targetClass = signature.getDeclaringType();
        String className = targetClass.getSimpleName();
        String url = extractUrl(targetClass, method);
     */
    private String methodName;
    private String className;
    private String url;
    private String layer;

}
