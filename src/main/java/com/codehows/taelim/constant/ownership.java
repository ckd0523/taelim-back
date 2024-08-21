package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//소유권
public enum ownership {

    OWNED("소유"),
    LEASED("임대");
    private final String description;

}
