package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//자산기준
public enum assetBasis {

    COMMON("일반"),
    TISAX("TISAX");

    private final String description;
}
