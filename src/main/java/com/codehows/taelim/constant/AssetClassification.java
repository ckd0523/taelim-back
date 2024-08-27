package com.codehows.taelim.constant;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//자산분류
public enum AssetClassification {
    INFORMATION_PROTECTION_SYSTEM("정보보호시스템"),
    APPLICATION_PROGRAM("응용프로그램"),
    SOFTWARE("소프트웨어"),
    ELECTRONIC_INFORMATION("전자정보"),
    DOCUMENT("문서"),
    PATENTS_AND_TRADEMARKS("특허 및 상표"),
    ITSYSTEM_EQUIPMENT("IT 장비 - 시스템"),
    ITNETWORK_EQUIPMENT("IT 장비 – 네트워크"),
    TERMINAL("단말기"),
    FURNITURE("가구"),
    DEVICES("기기"),
    CAR("차량"),
    OTHERASSETS("기타");

    private final String description;

}
