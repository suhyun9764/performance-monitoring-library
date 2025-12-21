package com.suhyun.performancestarter.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
/**
 * Performance monitoring annotation applicable at both class and method levels.
 * <br>클래스 및 메서드 레벨에서 사용 가능한 성능 측정 어노테이션입니다.
 *
 * <p>Measures method execution time and displays it in the monitoring dashboard
 * with request-level granularity.
 * <br>요청 단위로 메서드 실행 시간을 측정하고 모니터링 대시보드에 표시합니다.</p>
 *
 * @since 1.0.0
 */
public @interface PerformanceMonitoring {

    /**
     * Enable query monitoring and N+1 detection.
     * <br>쿼리 모니터링 및 N+1 감지를 활성화합니다.
     *
     * <p>When enabled, tracks all database queries executed within the method scope
     * and detects potential N+1 query problems.
     * <br>활성화 시 메서드 스코프 내에서 실행되는 모든 데이터베이스 쿼리를 추적하고
     * N+1 쿼리 문제 가능성을 감지합니다.</p>
     *
     * <p><strong>Recommended for Service layer methods with transactional boundaries.
     * <br>트랜잭션 경계가 있는 Service 계층 메서드에 권장됩니다.</strong></p>
     *
     * @return true if query monitoring is enabled, false otherwise
     */
    boolean queryMonitoring() default false;
}