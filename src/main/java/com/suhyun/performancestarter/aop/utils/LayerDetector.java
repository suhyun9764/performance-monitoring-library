package com.suhyun.performancestarter.aop.utils;

import com.suhyun.performancestarter.aop.enums.Layer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Component
public class LayerDetector {
    public Layer detect(Class<?> targetClass) {
        // Controller 확인
        if (targetClass.isAnnotationPresent(RestController.class) ||
                targetClass.isAnnotationPresent(Controller.class)) {
            return Layer.CONTROLLER;
        }

        // Service 확인
        if (targetClass.isAnnotationPresent(Service.class)) {
            return Layer.SERVICE;
        }

        // Repository 확인
        if (targetClass.isAnnotationPresent(Repository.class)) {
            return Layer.REPOSITORY;
        }

        // 감지 실패
        return Layer.UNKNOWN;
    }
}
