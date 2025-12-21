package com.suhyun.performancestarter.aop.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class UrlExtractor {
    public String extractUrl(Class<?> targetClass, Method method) {
        HttpMethodAndPath methodAndPath = extractHttpMethodAndPath(method);

        if (methodAndPath == null) {
            return null;
        }

        // 클래스 레벨 base path
        String basePath = extractClassLevelPath(targetClass);

        // 최종 URL 조합
        String fullPath = basePath + methodAndPath.path;
        return methodAndPath.httpMethod + " " + fullPath;
    }

    private HttpMethodAndPath extractHttpMethodAndPath(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            String httpMethod = mapping.method().length > 0 ? mapping.method()[0].name() : "GET";
            String path = extractPath(mapping.value(), mapping.path());
            return new HttpMethodAndPath(httpMethod, path);
        }

        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            String path = extractPath(mapping.value(), mapping.path());
            return new HttpMethodAndPath("GET", path);
        }

        if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mapping = method.getAnnotation(PostMapping.class);
            String path = extractPath(mapping.value(), mapping.path());
            return new HttpMethodAndPath("POST", path);
        }

        if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping mapping = method.getAnnotation(PutMapping.class);
            String path = extractPath(mapping.value(), mapping.path());
            return new HttpMethodAndPath("PUT", path);
        }

        if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
            String path = extractPath(mapping.value(), mapping.path());
            return new HttpMethodAndPath("DELETE", path);
        }

        if (method.isAnnotationPresent(PatchMapping.class)) {
            PatchMapping mapping = method.getAnnotation(PatchMapping.class);
            String path = extractPath(mapping.value(), mapping.path());
            return new HttpMethodAndPath("PATCH", path);
        }

        return null;
    }

    private String extractClassLevelPath(Class<?> targetClass) {
        if (targetClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classMapping = targetClass.getAnnotation(RequestMapping.class);
            return extractPath(classMapping.value(), classMapping.path());
        }
        return "";
    }

    private String extractPath(String[] values, String[] paths) {
        if (values.length > 0) {
            return values[0];
        }
        if (paths.length > 0) {
            return paths[0];
        }
        return "";
    }

    private static class HttpMethodAndPath {
        final String httpMethod;
        final String path;

        HttpMethodAndPath(String httpMethod, String path) {
            this.httpMethod = httpMethod;
            this.path = path;
        }
    }
}