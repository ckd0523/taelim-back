package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//소유권
public enum Ownership {

    OWNED("소유"),
    LEASED("임대");
    private final String description;

//    Ownership(String description) {
//        this.description = description;
//    }
//
//    @JsonValue
//    public String getDescription(){
//        return description;
//    }
//
//    @JsonCreator
//    public static Ownership from(String value){
//        for(Ownership ownership : Ownership.values()) {
//            if(ownership.description.equals(value)) {
//                return ownership;
//            }
//        }
//        throw new IllegalArgumentException("잘못된 값: " + value);
//    }

}
