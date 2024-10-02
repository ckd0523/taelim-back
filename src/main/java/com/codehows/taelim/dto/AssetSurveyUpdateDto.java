package com.codehows.taelim.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssetSurveyUpdateDto {
    private boolean requestType; // 정위치 유무 : true, 상태 : false
    private Long infoNo;
    private boolean updateValue;
    private String content;
}
