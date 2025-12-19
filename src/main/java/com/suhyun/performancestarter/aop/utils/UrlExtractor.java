package com.suhyun.performancestarter.aop.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class UrlExtractor {
    /**
     * HTTP 메서드 + URL 추출
     *
     * @param targetClass 대상 클래스
     * @param method 대상 메서드
     * @return "GET /api/posts" 형식의 문자열, Controller가 아니면 null
     */
    public String extractUrl(Class<?> targetClass, Method method) {
        // Controller가 아니면 URL 없음
        // HTTP 메서드와 경로 추출
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

    /**
     * 메서드 레벨의 HTTP 메서드와 경로 추출
     */
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

    /**
     * 클래스 레벨의 base path 추출
     */
    private String extractClassLevelPath(Class<?> targetClass) {
        if (targetClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classMapping = targetClass.getAnnotation(RequestMapping.class);
            return extractPath(classMapping.value(), classMapping.path());
        }
        return "";
    }

    /**
     * value 또는 path 배열에서 첫 번째 값 추출
     */
    private String extractPath(String[] values, String[] paths) {
        if (values.length > 0) {
            return values[0];
        }
        if (paths.length > 0) {
            return paths[0];
        }
        return "";
    }

    /**
     * HTTP 메서드와 경로를 담는 내부 클래스
     */
    private static class HttpMethodAndPath {
        final String httpMethod;
        final String path;

        HttpMethodAndPath(String httpMethod, String path) {
            this.httpMethod = httpMethod;
            this.path = path;
        }
    }
}