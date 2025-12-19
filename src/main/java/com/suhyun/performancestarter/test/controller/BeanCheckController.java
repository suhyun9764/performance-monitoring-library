package com.suhyun.performancestarter.test.controller;
import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.test.repository.TestRepository;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BeanCheckController {
    
    @Autowired
    private ApplicationContext context;
    
    @GetMapping("/debug/check-repository")
    public Map<String, Object> checkRepository() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 모든 Bean 개수
        result.put("totalBeans", context.getBeanDefinitionNames().length);
        
        // 2. Repository로 끝나는 Bean
        List<String> repoBeans = Arrays.stream(context.getBeanDefinitionNames())
            .filter(name -> name.toLowerCase().endsWith("repository"))
            .collect(Collectors.toList());
        result.put("repositoryBeans", repoBeans);
        
        // 3. JpaRepository 타입 Bean
        Map<String, ?> jpaRepos = context.getBeansOfType(JpaRepository.class);
        result.put("jpaRepositoryBeans", jpaRepos.keySet());
        
        // 4. TestRepository 직접 확인
        try {
            Object bean = context.getBean("testRepository");
            result.put("testRepository", "존재: " + bean.getClass().getName());
        } catch (NoSuchBeanDefinitionException e) {
            result.put("testRepository", "없음: " + e.getMessage());
        }
        
        // 5. 타입으로 찾기
        try {
            TestRepository repo = context.getBean(TestRepository.class);
            result.put("testRepositoryByType", "존재: " + repo.getClass().getName());
        } catch (Exception e) {
            result.put("testRepositoryByType", "없음: " + e.getMessage());
        }

        try {
            TestRepository repo = context.getBean(TestRepository.class);
            result.put("testRepositoryClass", repo.getClass().getName());

            // Proxy인지 확인
            boolean isProxy = repo.getClass().getName().contains("Proxy");
            result.put("isProxy", isProxy);

            // 인터페이스 확인
            Class<?>[] interfaces = repo.getClass().getInterfaces();
            List<String> interfaceNames = Arrays.stream(interfaces)
                    .map(Class::getName)
                    .collect(Collectors.toList());
            result.put("interfaces", interfaceNames);

            // PerformanceMonitoring 어노테이션 확인
            boolean hasAnnotation = Arrays.stream(interfaces)
                    .anyMatch(i -> i.isAnnotationPresent(PerformanceMonitoring.class));
            result.put("hasPerformanceMonitoring", hasAnnotation);

            // AOP Advisor 확인 (CGLIB/JDK Proxy)
            if (repo instanceof Advised) {
                Advised advised = (Advised) repo;
                result.put("advisors", advised.getAdvisors().length);
            }

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        return result;
    }
}