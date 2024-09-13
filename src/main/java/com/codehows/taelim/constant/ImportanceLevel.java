package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//중요성 등급
public enum ImportanceLevel {
    GRADE_A("A급"),
    GRADE_B("B급"),
    GRADE_C("C급");
    private String description;

    ImportanceLevel(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static ImportanceLevel from(String value){
        for(ImportanceLevel importanceLevel : ImportanceLevel.values()) {
            if(importanceLevel.description.equals(value)) {
                return importanceLevel;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
