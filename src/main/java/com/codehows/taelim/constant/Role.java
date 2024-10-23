package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//권한
public enum Role {
    ROLE_USER("일반사용자"),
    ROLE_ASSET_MANAGER("자산담당자"),
    ROLE_ADMIN("관리자");
    private final String description;
}
