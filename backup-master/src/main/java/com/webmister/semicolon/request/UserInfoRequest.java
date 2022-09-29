package com.webmister.semicolon.request;

import com.webmister.semicolon.enumclass.UserStatus;
import lombok.Data;

@Data
public class UserInfoRequest {

    private String userEmail;
    private String password;
    private String decodedPassword;
    private String userNickName;
    private UserStatus userUniqueID;
    private String userProfileImageUrl;
    private String userDescription;
    private String refreshToken;

}
