package com.suhyun.performancestarter.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueryInfo {
    private final int totalQueryCount;
    private final boolean hasNPlusOne;
    private final String nPlusOnePattern;
    private final Integer nPlusOneCount;
}