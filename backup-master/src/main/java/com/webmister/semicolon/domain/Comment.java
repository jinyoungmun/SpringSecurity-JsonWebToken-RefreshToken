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
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_date")
    private LocalDateTime commentCreateTime;

    @Column(name = "update_date")
    private LocalDateTime commentUpdateTime;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "userInfo_id")
    private UserInfo userInfo;

    @PrePersist
    public void CommentCreatedDate() {
        this.commentCreateTime = LocalDateTime.now();
    }

    public void CommentUpdatedDate() {
        this.commentUpdateTime = LocalDateTime.now();
    }
}
