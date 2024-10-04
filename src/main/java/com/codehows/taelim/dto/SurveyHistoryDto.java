package com.codehows.taelim.dto;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SurveyHistoryDto {

    private Long assetSurveyDetailNo;
    private Long assetNo;
    private String assetCode;
    private String assetName;
    private Long round;
    private AssetLocation assetSurveyLocation;
    private LocalDate assetSurveyStartDate;
    private LocalDate assetSurveyEndDate;
    private String assetSurveyBy;

    private Boolean exactLocation;
    private Boolean assetStatus;
    private String assetSurveyContent;
}
