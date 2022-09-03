package com.webmister.semicolon.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String reportImageUrl;

    @Column
    private int likeCount;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column
    private LocalDateTime reportCreateTime;

    @Column
    private LocalDateTime reportUpdateTime;

    @OneToMany(mappedBy = "report",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") //댓글 정렬
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userInfo")
    @JsonBackReference
    private UserInfo userInfo;


    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    @PrePersist
    public void ReportCreatedDate() {
        this.reportCreateTime = LocalDateTime.now();
    }

    public void ReportUpdatedDate() {
        this.reportUpdateTime = LocalDateTime.now();
    }
}
