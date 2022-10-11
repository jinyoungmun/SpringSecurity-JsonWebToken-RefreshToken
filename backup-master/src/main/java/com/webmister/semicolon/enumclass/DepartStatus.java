package com.webmister.semicolon.enumclass;

import lombok.Getter;

@Getter
public enum DepartStatus {

    FREE("자유", Long.valueOf(0)),
    CONTROLLER("제어", Long.valueOf(1)),
    CIRCUIT("회로", Long.valueOf(2)),
    INFORMATION("통신", Long.valueOf(3)),
    AI("인공지능", Long.valueOf(4));

    String depart;
    Long departNum;
    DepartStatus(String depart, Long departNum) {
        this.depart = depart;
        this.departNum = departNum;
    }
}
