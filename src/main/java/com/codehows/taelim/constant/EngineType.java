package com.codehows.taelim.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
//엔진 형식
public enum EngineType {
    GASOLINE("가솔린"),
    DIESEL("디젤"),
    HYBRID("하이브리드"),
    ELECTRIC("전기");
    private final String description;

}
