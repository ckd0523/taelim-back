package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//특허세목
public enum patentItem {
    COMPOSITE_MATERIALS("복합재"),
    CORPORATE_VENTURE("사내벤처");
    private final String description;
}
