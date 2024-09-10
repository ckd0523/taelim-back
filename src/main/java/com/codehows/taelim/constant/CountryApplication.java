package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//출원국가
public enum CountryApplication {
    KOREA("한국"),
    USA("미국"),
    JAPAN("일본"),
    CHINA("중국"),
    GERMANY("독일");
    private String description;

    CountryApplication(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static CountryApplication from(String value){
        for(CountryApplication countryApplication : CountryApplication.values()) {
            if(countryApplication.description.equals(value)) {
                return countryApplication;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
