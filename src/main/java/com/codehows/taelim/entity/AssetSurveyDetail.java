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
public class AssetSurveyDetail {

    @Id
    @Column(name = "assetSurveyDetailNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetSurveyDetailNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSurveyNo")
    private AssetSurveyHistory assetSurveyNo;

    private Boolean assetStatus;
    private Boolean exactLocation;
    private String assetSurveyContent;

}
