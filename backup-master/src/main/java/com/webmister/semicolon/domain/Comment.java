package com.webmister.semicolon.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @Column(name = "commentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long commentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "createdDate")
    private LocalDateTime commentCreateTime;

    @Column(name = "updateDate")
    private LocalDateTime commentUpdateTime;

    @ManyToOne
    @JoinColumn(name = "reportId")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "userInfoId")
    private UserInfo userInfo;

    @PrePersist
    public void CommentCreatedDate() {
        this.commentCreateTime = LocalDateTime.now();
    }

    public void CommentUpdatedDate() {
        this.commentUpdateTime = LocalDateTime.now();
    }
}
