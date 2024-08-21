package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//특허/상표 상태
public enum patentTrademarkStatus {

    PCT_APPLICATION("PCT 출원"),
    APPLICATION("출원"),
    REGISTERED("등록"),
    EXPIRED("만료");
    private final String description;
}
