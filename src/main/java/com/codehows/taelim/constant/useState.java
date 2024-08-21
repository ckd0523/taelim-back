package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//사용상태
public enum useState {

    NEW("신규"),
    IN_USE("사용중"),
    UNDER_MAINTENANCE("유지 관리 중 or 보수 작업 중"),
    RESERVED("예비"),
    RETIRED_DISCARDED("퇴직/폐기");
    private final String description;

}
