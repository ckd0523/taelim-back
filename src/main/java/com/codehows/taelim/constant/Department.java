package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//부서
public enum Department {



    MANAGEMENT_PLANNING_OFFICE("경영기획실"),
    MANAGEMENT_TEAM("관리팀"),
    SALES_TEAM("영업팀"),
    PURCHASE_TEAM("구매팀"),

    QUALITY_TEAM("품질팀"),
    PRODUCTION_TEAM("생산팀"),

    TECHNOLOGY_RESEARCH_TEAM ("기술연구소"),
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

        try{
            return Department.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }

}
