package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//특허세목
public enum PatentItem {
    COMPOSITE_MATERIALS("복합재"),
    CORPORATE_VENTURE("사내벤처");

    private  String description;

    PatentItem(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static PatentItem from(String value){
        for(PatentItem patentItem : PatentItem.values()) {
            if(patentItem.description.equals(value)) {
                return patentItem;
            }
        }
        try{
            return PatentItem.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }
}
