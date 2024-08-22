package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//차량 종류
public enum CarType {
    SEDAN("승용차"),
    SUV("SUV"),
    TRUCK("트럭"),
    VAN("밴");
    private final String description;
}
