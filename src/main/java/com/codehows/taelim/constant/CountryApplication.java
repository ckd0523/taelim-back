package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//출원국가
public enum CountryApplication {
    KOREA("한국"),
    USA("미국"),
    JAPAN("일본"),
    CHINA("중국"),
    GERMANY("독일");
    private final String description;
}
