package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//조사 상태
public enum SurveyStatus {
    IN_PROGRESS("조사중"),
    COMPLETED("조사완료");
    private final String description;
}
