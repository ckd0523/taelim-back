package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//보수 상태
public enum RepairStatus {
    COMPLETED("완료"),
    IN_PROGRESS("진행중");
    private final String description;
}
