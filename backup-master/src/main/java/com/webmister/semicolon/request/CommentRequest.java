package com.webmister.semicolon.request;

import lombok.Data;

@Data
public class CommentRequest {
    private String comment;
    private Long userId;
    private Long reportId;
}
