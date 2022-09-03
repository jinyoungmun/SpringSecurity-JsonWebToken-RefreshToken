package com.webmister.semicolon.request;

import lombok.Data;

@Data
public class UploadRequest {
    String title;
    String contents;
    int likeCount;
    String reportImageUrl;

}
