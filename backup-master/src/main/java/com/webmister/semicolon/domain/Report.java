package com.webmister.semicolon.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.webmister.semicolon.enumclass.DepartStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @Column(name = "reportId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reportId;

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
    @OrderBy("commentId asc") //댓글 정렬
    private List<Comment> comments;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartStatus userDepartStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userInfoId")
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
