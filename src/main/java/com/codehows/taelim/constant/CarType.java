package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//차량 종류
public enum CarType {
    SEDAN("승용차"),
    SUV("SUV"),
    TRUCK("트럭"),
    VAN("밴");
    private String description;

    CarType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static CarType from(String value){
        for(CarType carType : CarType.values()) {
            if(carType.description.equals(value)) {
                return carType;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
