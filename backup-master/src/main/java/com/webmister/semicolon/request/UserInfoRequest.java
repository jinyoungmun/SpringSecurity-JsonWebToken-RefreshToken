package com.webmister.semicolon.request;

import com.webmister.semicolon.dto.TokenDto;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class UserInfoRequest {

    private String userEmail;
    private String password;
    private String userNickName;
    private String userUniqueID;
    private String userProfileImageUrl;
    private String userDescription;
    private String refreshToken;
}
