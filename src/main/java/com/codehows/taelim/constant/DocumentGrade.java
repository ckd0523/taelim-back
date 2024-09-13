package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//문서등급
public enum DocumentGrade {
    CONFIDENTIAL("대외비"),
    INTERNAL("내부용"),
    PUBLIC("일반");
    private final String description;

//    DocumentGrade(String description) {
//        this.description = description;
//    }

//    @JsonValue
//    public String getDescription(){
//        return description;
//    }
//
//    @JsonCreator
//    public static DocumentGrade from(String value){
//        for(DocumentGrade documentGrade : DocumentGrade.values()) {
//            if(documentGrade.description.equals(value)) {
//                return documentGrade;
//            }
//        }
//        throw new IllegalArgumentException("잘못된 값: " + value);
//    }

}
