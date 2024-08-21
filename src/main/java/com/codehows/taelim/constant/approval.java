package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//승인여부
public enum approval {

    UNCONFIRMED("미확인"),
    APPROVE("승인"),
    REFUSAL("거절");
    private final String description;

}
