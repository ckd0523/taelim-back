package com.codehows.taelim.dto;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.AssetSurveyHistory;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetSurvey {



    private Long assetSurveyNo;
    private String assetSurveyBy;
    private Long round;
    private AssetLocation assetSurveyLocation;
    private LocalDate assetSurveyStartDate;
    private LocalDate assetSurveyEndDate;
    private Boolean surveyStatus;

    private Long assetSurveyDetailNo;
    private Boolean assetStatus;
    private Boolean exactLocation;
    private String assetSurveyContent;

}
