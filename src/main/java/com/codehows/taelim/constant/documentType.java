package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//문서형태
public enum documentType {

    GENERAL_DOCUMENT("일반문서"),
    CONTRACTS_AND_LEGAL_DOCUMENTS("계약 및 법적문서"),
    REPORTS_AND_PRESENTATIONS("보고서 및 프레젠테이션"),
    FORMS_AND_TEMPLATES("양식 및 서식");
    private final String description;

}
