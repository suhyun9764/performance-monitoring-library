package com.suhyun.performancestarter.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    private String password;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Builder.Default
    private int viewCount = 0;
    @CreationTimestamp
//    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
//    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
    fetch = FetchType.EAGER
    )
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public static Post create(String title, String content, String author, String password) {
        return Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .password(password)
                .build();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(password);
    }

//    public void deleteComment(Comment comment) {
//        comments.remove(comment);
//    }
}
