package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//중요성 등급
public enum importanceLevel {
    GRADE_A("A급"),
    GRADE_B("B급"),
    GRADE_C("C급");
    private final String description;
}
