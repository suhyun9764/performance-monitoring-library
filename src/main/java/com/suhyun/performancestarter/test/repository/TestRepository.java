package com.suhyun.performancestarter.test.repository;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.test.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PerformanceMonitoring(queryMonitoring = true)
public interface TestRepository extends JpaRepository<Post,Long> {
    @Override
    List<Post> findAll();  // ← 직접 선언!

    List<Post> findByTitle(String title);
}
