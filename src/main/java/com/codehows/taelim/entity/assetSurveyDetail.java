package com.codehows.taelim.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assetSurveyDetail")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class assetSurveyDetail {

    @Id
    @Column(name = "InfoNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InfoNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSurveyNo")
    private assetSurveyHistory assetSurveyNo;

    private Boolean assetStatus;
    private Boolean exactLocation;
    private String assetSurveyContent;

}
