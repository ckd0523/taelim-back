package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//권한
public enum Role {
    USER("일반사용자"),
    ASSET_MANAGER("자산담당자"),
    ADMIN("관리자");
    private final String description;
}
