package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//보수 사진 전/후
public enum RepairType {
    BEFORE_REPAIR("보수전"),
    AFTER_REPAIR("보수후");
    private String description;

    RepairType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static RepairType from(String value){
        for(RepairType repairType : RepairType.values()) {
            if(repairType.description.equals(value)) {
                return repairType;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
