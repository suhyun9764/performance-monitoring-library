package com.suhyun.performancestarter.aop.utils;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.aop.dto.MethodInfo;
import com.suhyun.performancestarter.enums.Layer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class MethodInfoExtractor {
    private final UrlExtractor urlExtractor;
    private final LayerDetector layerDetector;

    public MethodInfo extract(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = signature.getDeclaringType();

        Layer layer = layerDetector.detect(targetClass);
        String url = null;
        if(layer.equals(Layer.CONTROLLER))
            url = urlExtractor.extractUrl(targetClass, method);

        return MethodInfo.builder()
                .methodName(method.getName())
                .className(targetClass.getSimpleName())
                .layer(layer.name())
                .queryMonitoring(extractQueryMonitoring(method,targetClass))
                .url(url)
                .build();

    }

    private boolean extractQueryMonitoring(Method method, Class<?> targetClass) {
        PerformanceMonitoring methodAnnotation =
                method.getAnnotation(PerformanceMonitoring.class);
        if (methodAnnotation != null) {
            return methodAnnotation.queryMonitoring();
        }

        PerformanceMonitoring classAnnotation =
                targetClass.getAnnotation(PerformanceMonitoring.class);
        if (classAnnotation != null) {
            return classAnnotation.queryMonitoring();
        }

        return false;
    }
}
