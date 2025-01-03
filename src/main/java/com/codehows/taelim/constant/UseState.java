package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//사용상태
public enum UseState {

    NEW("신규"),
    IN_USE("사용중"),
    UNDER_MAINTENANCE("유지 관리 중 or 보수 작업 중"),
    RESERVED("예비"),
    RETIRED_DISCARDED("퇴직/폐기");
    private String description;

    UseState(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static UseState from(String value){
        for(UseState useState : UseState.values()) {
            if(useState.description.equals(value)) {
                return useState;
            }
        }
        try {
            return UseState.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }

}
