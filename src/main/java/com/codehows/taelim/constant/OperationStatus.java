package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//가동여부
public enum OperationStatus {
    OPERATING("가동중"),
    NOT_OPERATING("미가동"),
    MALFUNCTION("고장");
    private final String description;
}
