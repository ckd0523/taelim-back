package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//부서
public enum Department {

    IT_DEPARTMENT("IT부"),
    ADMINISTRATIVE_DEPARTMENT("관리부"),
    SALES_DEPARTMENT("영업부"),
    MARKETING_DEPARTMENT("마케팅부"),
    PRODUCTION_DEPARTMENT("생산부"),
    OPERATIONS_DEPARTMENT("운영부"),
    HUMAN_RESOURCES_DEPARTMENT("인사부");
    private final String description;

}
