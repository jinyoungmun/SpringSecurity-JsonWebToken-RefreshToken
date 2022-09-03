package com.webmister.semicolon.dto;

import com.webmister.semicolon.domain.Comment;
import com.webmister.semicolon.domain.Report;
import com.webmister.semicolon.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private Long id;
    private String comment;
    private UserInfo userInfo;
    private Report report;

}
