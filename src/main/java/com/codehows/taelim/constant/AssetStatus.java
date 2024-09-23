package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//자산 상태
public enum AssetStatus {
    NORMAL("정상"),
    DAMAGED("파손");
    private String description;

    AssetStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static AssetStatus from(String value){
        for(AssetStatus assetStatus : AssetStatus.values()) {
            if(assetStatus.description.equals(value)) {
                return assetStatus;
            }
        }
        try{
            return AssetStatus.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }
}
