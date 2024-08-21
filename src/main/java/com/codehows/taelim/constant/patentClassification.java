package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//특허분류
public enum patentClassification {
    NEW_MATERIALS("신소재"),
    INCUBATION("인큐베이션");
    private final String description;
}
