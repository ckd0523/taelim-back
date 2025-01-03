package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//보안관제
public enum SecurityControl {
    MONITORING("관제중"),
    ANOMALY_DETECTED("이상감지"),
    MONITORING_COMPLETED("관제완료"),
    NULL("");
    private final String description;



    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static SecurityControl from(String value){
        for(SecurityControl securityControl : SecurityControl.values()) {
            if(securityControl.description.equals(value)) {
                return securityControl;
            }
        }
        try{
            return SecurityControl.valueOf(value);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }
}
