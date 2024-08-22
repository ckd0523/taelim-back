package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//모니터 포함여부
public enum MonitorIncluded {
    YES("포함"),
    NO("미포함");
    private final String description;
}
