package com.webmister.semicolon.response;

import com.webmister.semicolon.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String comment;
    private LocalDateTime commentCreatedTime;
    private LocalDateTime commentUpdateTime;
    private String nickname;
    private Long reportId;


    public CommentResponse(Comment comment)
    {
        this.id = comment.getCommentId();
        this.comment = comment.getComment();
        this.commentCreatedTime = comment.getCommentCreateTime();
        this.commentUpdateTime = comment.getCommentUpdateTime();
        this.nickname = comment.getUserInfo().getUserNickName();
        this.reportId = comment.getReport().getReportId();
    }
}
