package com.codehows.taelim.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//첨부파일 구분
public enum FileType {

    PHOTO("사진"),
    WARRANTY_DETAILS("보증세부사항"),
    USER_MANUAL("사용자 메뉴얼"),
    PATENT_DOCUMENTS("특허관련문서");
    private String description;

    FileType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription(){
        return description;
    }

    @JsonCreator
    public static FileType from(String value){
        for(FileType fileType : FileType.values()) {
            if(fileType.description.equals(value)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("잘못된 값: " + value);
    }
}