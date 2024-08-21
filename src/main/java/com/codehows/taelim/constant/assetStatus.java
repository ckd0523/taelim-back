package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//자산 상태
public enum assetStatus {
    NORMAL("정상"),
    DAMAGED("파손");
    private final String description;
}
