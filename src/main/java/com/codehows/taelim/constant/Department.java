package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//부서
public enum Department {


    IT_DEPARTMENT("IT부"),
    ADMINISTRATIVE_DEPARTMENT("관리부"),
    SALES_DEPARTMENT("영업부"),
    MARKETING_DEPARTMENT("마케팅부"),
    PRODUCTION_DEPARTMENT("생산부"),
    OPERATIONS_DEPARTMENT("운영부"),
    HUMAN_RESOURCES_DEPARTMENT("인사부"),
    NULL("N/A");
    private String description;

    Department(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static Department from(String value){
        for(Department department : Department.values()) {
            if(department.description.equals(value)) {
                return department;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }

}
