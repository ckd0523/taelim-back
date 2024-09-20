package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//자산 위치
public enum AssetLocation {

    MAIN_B1_DOCUMENT_STORAGE("본관 지하 문서고"),
    MAIN_1F("본관 1층"),
    MAIN_1F_RECEPTION_ROOM("본관 1층 접견실"),
    MAIN_2F("본관 2층"),
    MAIN_2F_PRESIDENT_OFFICE("본관 2층 사장실"),
    MAIN_2F_RESEARCH_OFFICE("본관 2층 기술연구소 사무실"),
    MAIN_2F_CONFERENCE_ROOM("본관 2층 대회의실"),
    MAIN_2F_CEO_OFFICE("본관 2층 대표이사실"),
    MAIN_3F_STORAGE("본관 3층 창고"),
    MDCG("MDCG 천장"),
    FACTORY_BUILDING("공장동");
    private String description;

    AssetLocation(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static AssetLocation from(String value){
        for(AssetLocation assetLocation : AssetLocation.values()) {
            if(assetLocation.description.equals(value)) {
                return assetLocation;
            }
        }
        try{
            return AssetLocation.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }




}
