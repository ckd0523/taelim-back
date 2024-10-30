package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//특허분류
public enum PatentClassification {
    NEW_MATERIALS("신소재"),
    INCUBATION("인큐베이션");
    private String description;

    PatentClassification(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static PatentClassification from(String value){
        for(PatentClassification patentClassification : PatentClassification.values()) {
            if(patentClassification.description.equals(value)) {
                return patentClassification;
            }
        }
        try{
            return PatentClassification.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }
}
