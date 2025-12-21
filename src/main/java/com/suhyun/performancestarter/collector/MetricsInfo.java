package com.suhyun.performancestarter.collector;

import com.suhyun.performancestarter.dto.MetricsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MetricsInfo {
    private String traceId;
    private String requestUrl;
    private MetricsDto root;
}
