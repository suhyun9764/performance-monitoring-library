package com.suhyun.performancestarter.aop.utils;

import com.suhyun.performancestarter.aop.enums.Layer;
import com.suhyun.performancestarter.aop.dto.MethodInfo;
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
        String methodName = signature.getMethod().getName();
        Class<?> targetClass = signature.getDeclaringType();
        String className = targetClass.getSimpleName();
        Layer layer = layerDetector.detect(targetClass);
        String url = null;
        if(layer.equals(Layer.CONTROLLER))
            url = urlExtractor.extractUrl(targetClass, method);

        return MethodInfo.builder()
                .methodName(methodName)
                .className(className)
                .layer(layer.name())
                .url(url)
                .build();

    }
}
