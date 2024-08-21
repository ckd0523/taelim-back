package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//첨부파일 구분
public enum fileType {

    PHOTO("사진"),
    WARRANTY_DETAILS("보증세부사항"),
    USER_MANUAL("사용자 메뉴얼"),
    PATENT_DOCUMENTS("특허관련문서");
    private final String description;
}