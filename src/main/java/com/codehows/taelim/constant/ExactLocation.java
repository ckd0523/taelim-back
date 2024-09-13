package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//정위치 유/무
public enum ExactLocation {
    LOCATED("정위치 유"),
    NOT_LOCATED("정위치 무");
    private String description;

    ExactLocation(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static ExactLocation from(String value){
        for(ExactLocation exactLocation : ExactLocation.values()) {
            if(exactLocation.description.equals(value)) {
                return exactLocation;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
