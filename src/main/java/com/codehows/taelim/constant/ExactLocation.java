package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//정위치 유/무
public enum ExactLocation {
    LOCATED("정위치 유"),
    NOT_LOCATED("정위치 무");
    private final String description;
}
