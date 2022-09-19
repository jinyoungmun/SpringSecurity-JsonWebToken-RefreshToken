package com.webmister.semicolon.request;

import lombok.Data;

@Data
public class TokenRequest {

    private String accessToken;
    private String refreshToken;
}
