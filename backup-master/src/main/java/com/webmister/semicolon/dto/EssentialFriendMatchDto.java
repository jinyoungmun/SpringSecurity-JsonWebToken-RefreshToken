package com.webmister.semicolon.dto;

import com.webmister.semicolon.domain.UserInfo;
import lombok.Data;

@Data
public class EssentialFriendMatchDto {
    UserInfo postFriendId;
    UserInfo receiveFriendId;


    public EssentialFriendMatchDto(UserInfo postFriendId, UserInfo receiveFriendId){
        this.postFriendId = postFriendId;
        this.receiveFriendId = receiveFriendId;
    }
}
