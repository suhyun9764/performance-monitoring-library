package com.suhyun.performancestarter.test.service;

import com.suhyun.performancestarter.annotation.PerformanceMonitoring;
import com.suhyun.performancestarter.test.entity.Comment;
import com.suhyun.performancestarter.test.entity.Post;
import com.suhyun.performancestarter.test.repository.TestJdbcRepositoryImpl;
import com.suhyun.performancestarter.test.repository.TestRepository;
import com.suhyun.performancestarter.test.repository.TestRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@PerformanceMonitoring(queryMonitoring = true)
@RequiredArgsConstructor
public class TestService2  {
    private final TestRepository testRepository;
    private final TestRepository2 testRepository2;
    private final TestJdbcRepositoryImpl testJdbcRepository;

    @Transactional
    public void test() throws InterruptedException {
        get();
//        testRepository.findByTitle("1330L");
//        testRepository2.test();
    }

    private void get() {
        List<Post> all = testRepository.findAll();
        for(Post post : all){
            List<Comment> comments = post.getComments();
            for(Comment comment : comments)
                comment.getContent();
        }
    }

}
