package com.suhyun.performancestarter.aop.dto;

import com.suhyun.performancestarter.dto.NPlusOneIssue;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class QueryInfo {
    private final int totalQueryCount;
    private final boolean hasNPlusOne;
    // ✅ 변경
    @Builder.Default
    private final List<NPlusOneIssue> nPlusOneIssues = new ArrayList<>();
}