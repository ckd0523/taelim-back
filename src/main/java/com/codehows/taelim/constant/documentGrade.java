package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//문서등급
public enum documentGrade {
    CONFIDENTIAL("대외비"),
    INTERNAL("내부용"),
    PUBLIC("일반");
    private final String description;

}
