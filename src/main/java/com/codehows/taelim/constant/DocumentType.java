package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//문서형태
public enum DocumentType {

    GENERAL_DOCUMENT("일반문서"),
    CONTRACTS_AND_LEGAL_DOCUMENTS("계약 및 법적문서"),
    REPORTS_AND_PRESENTATIONS("보고서 및 프레젠테이션"),
    FORMS_AND_TEMPLATES("양식 및 서식");
    private String description;

    DocumentType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static DocumentType from(String value){
        for(DocumentType documentType : DocumentType.values()) {
            if(documentType.description.equals(value)) {
                return documentType;
            }
        }
        try{
            return DocumentType.valueOf(value);
        }catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값: " + value);
        }
    }

}
