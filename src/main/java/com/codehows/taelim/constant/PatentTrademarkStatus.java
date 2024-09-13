package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//특허/상표 상태
public enum PatentTrademarkStatus {

    PCT_APPLICATION("PCT 출원"),
    APPLICATION("출원"),
    REGISTERED("등록"),
    EXPIRED("만료");
    private String description;

    PatentTrademarkStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static PatentTrademarkStatus from(String value){
        for(PatentTrademarkStatus patentTrademarkStatus : PatentTrademarkStatus.values()) {
            if(patentTrademarkStatus.description.equals(value)) {
                return patentTrademarkStatus;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
