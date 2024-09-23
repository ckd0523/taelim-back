package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//보수 상태
public enum RepairStatus {
    COMPLETED("완료"),
    IN_PROGRESS("진행중");
    private final String description;

//    RepairStatus(String description) {
//        this.description = description;
//    }
//
//    @JsonValue
//    public String getDescription(){
//        return description;
//    }
//
//    @JsonCreator
//    public static RepairStatus from(String value){
//        for(RepairStatus repairStatus : RepairStatus.values()) {
//            if(repairStatus.description.equals(value)) {
//                return repairStatus;
//            }
//        }
//        throw new IllegalArgumentException("잘못된 값: " + value);
//    }
}
