package com.codehows.taelim.entity;

import com.codehows.taelim.constant.assetLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "assetSurveyHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class assetSurveyHistory {

    @Id
    @Column(name = "assetSurveyNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetSurveyNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSurveyBy")
    private member assetSurveyBy;

    private Long round;
    private assetLocation assetSurveyLocation;
    private LocalDate assetSurveyStartDate;
    private LocalDate assetSurveyEndDate;
    private Boolean surveyStatus;

}
