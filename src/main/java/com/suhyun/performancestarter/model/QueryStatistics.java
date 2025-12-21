package com.suhyun.performancestarter.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QueryStatistics {
    private int totalQueryCount = 0;
    private List<String> queries = new ArrayList<>();
    private Map<String, Integer> queryPatternCount = new HashMap<>();

    private static final int DEFAULT_MIN_REPEAT_COUNT = 3;
    private static final double DEFAULT_MIN_REPEAT_RATIO = 0.3;

    public void addQuery(String sql){
        totalQueryCount++;
        queries.add(sql);
        String pattern = extractPattern(sql);
        queryPatternCount.merge(pattern,1,Integer::sum);
    }

    private String extractPattern(String sql) {
        return sql.replaceAll("\\d+", "?")
                .replaceAll("'[^']*'", "?")
                .replaceAll("\"[^\"]*\"", "?")
                .toLowerCase()
                .trim();
    }

    public boolean hasNPlusOne() {
        return hasNPlusOne(DEFAULT_MIN_REPEAT_COUNT, DEFAULT_MIN_REPEAT_RATIO);
    }

    public boolean hasNPlusOne(int minRepeatCount, double minRepeatRatio) {
        // 쿼리가 너무 적으면 판단 안 함
        if (totalQueryCount < 3) {
            return false;
        }

        for (int count : queryPatternCount.values()) {
            // 절대값 조건 && 비율 조건
            if (count >= minRepeatCount &&
                    count >= totalQueryCount * minRepeatRatio) {
                return true;
            }
        }

        return false;
    }

    public List<Map.Entry<String, Integer>> getSuspiciousPatterns() {
        return queryPatternCount.entrySet().stream()
                .filter(e -> e.getValue() >= DEFAULT_MIN_REPEAT_COUNT ||
                        e.getValue() >= totalQueryCount * DEFAULT_MIN_REPEAT_RATIO)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();
    }

}
