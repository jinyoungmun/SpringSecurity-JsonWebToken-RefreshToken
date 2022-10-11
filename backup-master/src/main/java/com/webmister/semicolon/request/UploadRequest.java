package com.webmister.semicolon.request;

import lombok.Data;

@Data
public class UploadRequest {
    String title;
    String contents;
    String reportImageUrl;
}
