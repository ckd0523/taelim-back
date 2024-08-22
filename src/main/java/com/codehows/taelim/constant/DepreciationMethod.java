package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//감가상각방법
public enum DepreciationMethod {

    FIXED_AMOUNT("정액법"),
    FIXED_RATE("정률법");
    private final String description;

}
