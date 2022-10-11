package com.webmister.semicolon.enumclass;

import lombok.Getter;

@Getter
public enum FriendStatus {

    FOLLOW("connect", Long.valueOf(1)),
    UNFOLLOW("unConnect", Long.valueOf(0));

    private String connectOrUnConnect;
    private Long number;

    FriendStatus(String connectOrUnConnect, Long number) {
        this.connectOrUnConnect = connectOrUnConnect;
        this.number = number;
    }

}
