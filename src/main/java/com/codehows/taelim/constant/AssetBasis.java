package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//자산기준
public enum AssetBasis {

    COMMON("일반"),
    TISAX("TISAX");

    private String description;

    AssetBasis(String description) {
        this.description = description;
    }

    @JsonValue
    public String getKoreanName() {
        return description;
    }

    @JsonCreator
    public static AssetBasis from(String value) {
        for (AssetBasis basis : AssetBasis.values()) {
            if (basis.description.equals(value)) {
                return basis;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}
