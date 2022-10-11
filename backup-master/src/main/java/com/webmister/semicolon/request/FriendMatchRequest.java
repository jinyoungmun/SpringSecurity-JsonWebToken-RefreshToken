package com.webmister.semicolon.request;

import com.webmister.semicolon.enumclass.FriendStatus;
import lombok.Data;

@Data
public class FriendMatchRequest {
    String receiveFriendNickname;
    FriendStatus friendStatus;
}
