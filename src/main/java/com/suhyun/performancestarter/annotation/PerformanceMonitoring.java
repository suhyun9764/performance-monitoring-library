package com.suhyun.performancestarter.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PerformanceMonitoring {
    String value() default "";
}
