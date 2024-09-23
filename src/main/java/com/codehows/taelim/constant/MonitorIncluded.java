package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//모니터 포함여부
public enum MonitorIncluded {
    YES("포함"),
    NO("미포함");
    private final String description;

//    MonitorIncluded(String description) {
//        this.description = description;
//    }
//
//    @JsonValue
//    public String getDescription(){
//        return description;
//    }
//
//    @JsonCreator
//    public static MonitorIncluded from(String value){
//        for(MonitorIncluded monitorIncluded : MonitorIncluded.values()) {
//            if(monitorIncluded.description.equals(value)) {
//                return monitorIncluded;
//            }
//        }
//        throw new IllegalArgumentException("잘못된 값: " + value);
//    }
}
