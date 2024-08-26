package com.codehows.taelim.dto;


import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilteredAssetDto {
    private Integer assetNo;
    private String assetClassification;
    private String assetBasis;
    private String assetCode;
    private String assetName;
    private String purpose;
    private String department;
    private String assetLocation;
    private String operationStatus;
    private String purchaseDate;
    private String manufacturingCompany;
    private String warrantyDetails;
    private String approval;
    private String attachment;
    // Getters and Setters
}

