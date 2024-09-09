package com.codehows.taelim.entity;

import com.codehows.taelim.constant.AssetLocation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assetSurveyHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssetSurveyHistory {

    @Id
    @Column(name = "assetSurveyNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetSurveyNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSurveyBy")
    private Member assetSurveyBy;

    private Long round;

    @Enumerated(EnumType.STRING)
    private AssetLocation assetSurveyLocation;

    private LocalDate assetSurveyStartDate;
    private LocalDate assetSurveyEndDate;
    private Boolean surveyStatus;

    @Builder
    public AssetSurveyHistory(Member member, Long round, AssetLocation assetSurveyLocation, LocalDate assetSurveyStartDate) {
        this.assetSurveyBy = member;
        this.round = round;
        this.assetSurveyLocation = assetSurveyLocation;
        this.assetSurveyStartDate = assetSurveyStartDate;
        this.surveyStatus = false;
    }
}
