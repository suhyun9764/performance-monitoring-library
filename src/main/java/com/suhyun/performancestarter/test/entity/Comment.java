    package com.suhyun.performancestarter.test.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDateTime;

    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String author;
        private String content;
        private String password;
        @CreationTimestamp
        private LocalDateTime createdAt;
        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @ManyToOne
        @JoinColumn(name = "post_id")
        private Post post;

        public static Comment create(String content, String author,String password) {
            return Comment.builder()
                    .author(author)
                    .content(content)
                    .password(password)
                    .build();
        }

        public void setPost(Post post) {
            this.post = post;
        }

        public boolean isMatchPassword(String password){
            return this.password.equals(password);
        }

        public void update(String content) {
            this.content = content;
        }
    }
