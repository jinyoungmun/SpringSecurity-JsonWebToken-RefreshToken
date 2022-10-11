package com.webmister.semicolon.request;

import com.webmister.semicolon.enumclass.DepartStatus;
import com.webmister.semicolon.enumclass.UserStatus;
import lombok.Data;

@Data
public class UserInfoRequest {

    private String userEmail;
    private String password;
    private String userNickname;
    private UserStatus userStatus;
    private String userProfileImageUrl;
    private String userDescription;
    private DepartStatus userDepartStatus;
    private String refreshToken;
    private String decodedPassword;

}
