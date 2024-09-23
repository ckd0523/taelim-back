package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//가동여부
public enum OperationStatus {
    OPERATING("가동중"),
    NOT_OPERATING("미가동"),
    MALFUNCTION("고장");
    private String description;

    OperationStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static OperationStatus from(String value){
        for(OperationStatus operationStatus : OperationStatus.values()) {
            if(operationStatus.description.equals(value)) {
                return operationStatus;
            }
        }
        try{
            return OperationStatus.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }
}
