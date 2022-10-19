package com.webmister.semicolon.dto;

import com.webmister.semicolon.domain.UserInfo;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class EssentialUserInfo {

    String password;
    String decodedPassword;
    String userEmail;
    String userNickName;
    String userUniqueId;
    String userProfileImageUrl;
    String userDescription;
    String refreshToken;

    private Set<AuthorityDto> authorityDtoSet;

    public EssentialUserInfo(UserInfo userInfo) {

        this.userEmail = getUserEmail();
        this.password = getPassword();
        this.decodedPassword = getDecodedPassword();
        this.userNickName = getUserNickName();
        this.userUniqueId = getUserUniqueId();


        if (userInfo.getUserProfileImageUrl() != null) {
            this.userProfileImageUrl = userInfo.getUserProfileImageUrl();
        }

        if (userInfo.getUserDescription() != null) {
            this.userDescription = userInfo.getUserDescription();
        }
        this.refreshToken = getRefreshToken();

        this.authorityDtoSet = userInfo.getAuthorities().stream().map(AuthorityDto::new).collect(Collectors.toSet());
    }
}
