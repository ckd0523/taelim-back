package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//보안관제
public enum securityControl {
    MONITORING("관제중"),
    ANOMALY_DETECTED("이상감지"),
    MONITORING_COMPLETED("관제완료");
    private final String description;
}
