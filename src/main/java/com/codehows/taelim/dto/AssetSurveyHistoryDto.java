package com.codehows.taelim.dto;

import com.codehows.taelim.constant.AssetLocation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
//뷰에 자산 조사 이력 보여줄 때 담아 줄 것들
public class AssetSurveyHistoryDto {
    private Long assetSurveyNo;
    private String uName;
    private AssetLocation assetSurveyLocation;
    private Long round;
    private Boolean surveyStatus;
    private LocalDate assetSurveyStartDate;
    private LocalDate assetSurveyEndDate;

    public AssetSurveyHistoryDto(Long assetSurveyNo, String uName, AssetLocation assetSurveyLocation, Long round, Boolean surveyStatus, LocalDate assetSurveyStartDate, LocalDate assetSurveyEndDate) {
        this.assetSurveyNo = assetSurveyNo;
        this.uName = uName;
        this.assetSurveyLocation = assetSurveyLocation;
        this.round = round;
        this.surveyStatus = surveyStatus;
        this.assetSurveyStartDate = assetSurveyStartDate;
        this.assetSurveyEndDate = assetSurveyEndDate;
    }
}
