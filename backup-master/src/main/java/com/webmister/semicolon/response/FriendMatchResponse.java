package com.webmister.semicolon.response;

import com.webmister.semicolon.enumclass.FriendStatus;
import lombok.Data;

@Data
public class FriendMatchResponse {
    String postFriendNickname;
    String receiveFriendNickname;
    FriendStatus friendStatus;
}
