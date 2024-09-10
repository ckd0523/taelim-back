package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//엔진 형식
public enum EngineType {
    GASOLINE("가솔린"),
    DIESEL("디젤"),
    HYBRID("하이브리드"),
    ELECTRIC("전기");
    private String description;

    EngineType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static EngineType from(String value){
        for(EngineType engineType : EngineType.values()) {
            if(engineType.description.equals(value)) {
                return engineType;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }

}
