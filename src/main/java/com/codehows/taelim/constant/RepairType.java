package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//보수 사진 전/후
public enum RepairType {
    BEFORE_REPAIR("보수전"),
    AFTER_REPAIR("보수후");
    private final String description;
}
