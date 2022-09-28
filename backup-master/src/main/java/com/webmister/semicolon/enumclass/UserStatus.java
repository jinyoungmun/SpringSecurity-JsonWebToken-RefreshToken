package com.webmister.semicolon.enumclass;

import lombok.Getter;

@Getter
public enum UserStatus {
    USER("User", Long.valueOf(1)),
    ADMIN("Admin", Long.valueOf(0));

    private String ROLE;
    private Long number;

    UserStatus(String ROLE, Long number) {
        this.ROLE = ROLE;
        this.number = number;
    }
}
