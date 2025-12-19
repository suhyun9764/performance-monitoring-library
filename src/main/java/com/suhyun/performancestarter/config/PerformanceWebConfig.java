package com.suhyun.performancestarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(name = "performance.monitoring.enabled", havingValue = "true", matchIfMissing = true)
public class PerformanceWebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /performance/** 경로를 static/performance/로 매핑
        registry.addResourceHandler("/performance/**")
                .addResourceLocations("classpath:/static/performance/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // /performance → /performance/index.html
        registry.addViewController("/performance")
                .setViewName("forward:/performance/index.html");
    }
}