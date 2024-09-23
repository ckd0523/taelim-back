package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//감가상각방법
public enum DepreciationMethod {

    FIXED_AMOUNT("정액법"),
    FIXED_RATE("정률법");
    private String description;

    DepreciationMethod(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static DepreciationMethod from(String value){
        for(DepreciationMethod depreciationMethod : DepreciationMethod.values()) {
            if(depreciationMethod.description.equals(value)) {
                return depreciationMethod;
            }
        }
        try{
            return DepreciationMethod.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }
}
