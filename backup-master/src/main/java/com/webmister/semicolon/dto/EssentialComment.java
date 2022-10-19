package com.webmister.semicolon.dto;

import com.webmister.semicolon.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EssentialComment {
    private Long id;
    private String comment;
    private LocalDateTime commentCreatedTime;
    private LocalDateTime commentUpdateTime;
    private String nickname;
    private Long reportId;


    public EssentialComment(Comment comment)
    {
        this.id = comment.getCommentId();
        this.comment = comment.getComment();
        this.commentCreatedTime = comment.getCommentCreateTime();
        this.commentUpdateTime = comment.getCommentUpdateTime();
        this.nickname = comment.getUserInfo().getUserNickName();
        this.reportId = comment.getReport().getReportId();
    }
}
