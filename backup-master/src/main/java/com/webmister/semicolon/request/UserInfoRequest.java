package com.webmister.semicolon.request;

import lombok.Data;

@Data
public class UserInfoRequest {

    private String userEmail;
    private String password;
    private String userNickName;
    private String userUniqueID;
    private String userProfileImageUrl;
    private String userDescription;
}
