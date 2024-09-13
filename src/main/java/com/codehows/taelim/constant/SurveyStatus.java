package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//조사 상태
public enum SurveyStatus {
    IN_PROGRESS("조사중"),
    COMPLETED("조사완료");
    private String description;

    SurveyStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static SurveyStatus from(String value){
        for(SurveyStatus surveyStatus : SurveyStatus.values()) {
            if(surveyStatus.description.equals(value)) {
                return surveyStatus;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
