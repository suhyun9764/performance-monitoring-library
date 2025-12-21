package com.suhyun.performancestarter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NPlusOneIssue {
    private String pattern;      // 쿼리 패턴
    private int count;           // 반복 횟수
}