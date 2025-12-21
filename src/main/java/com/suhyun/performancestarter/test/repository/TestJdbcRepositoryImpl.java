package com.suhyun.performancestarter.test.repository;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@PerformanceMonitoring
public class TestJdbcRepositoryImpl{
    private final JdbcTemplate jdbcTemplate;

    public void findByTitle(String title) {
        String sql = "SELECT id, title FROM post WHERE title = ?";

        jdbcTemplate.query(sql,
                rs -> {
                    while (rs.next()) {
                        rs.getLong("id");
                        rs.getString("title");
                    }
                },
                title
        );
    }
}
