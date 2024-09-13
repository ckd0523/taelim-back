package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//백업 범위
public enum BackUpScope {
    FULL("전체"),
    PARTIAL("일부");
    private final String description;

}
