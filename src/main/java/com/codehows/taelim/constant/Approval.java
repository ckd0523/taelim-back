package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//승인여부
public enum Approval {

    UNCONFIRMED("미확인"),
    APPROVE("승인"),
    REFUSAL("거절");
    private final String description;

//    Approval(String description) {
//        this.description = description;
//    }
//
//    @JsonValue
//    public String getDescription(){
//        return description;
//    }
//
//    @JsonCreator
//    public static Approval from(String value){
//        for(Approval approval : Approval.values()) {
//            if(approval.description.equals(value)) {
//                return approval;
//            }
//        }
//        throw new IllegalArgumentException("잘못된 값: " + value);
//    }
}
