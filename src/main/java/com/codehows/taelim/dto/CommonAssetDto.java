package com.codehows.taelim.dto;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonAssetDto {

    private Long assetNo;
    private AssetClassification assetClassification;
    private AssetBasis assetBasis;
    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;
    private Department department;
    private AssetLocation assetLocation;
    private String assetUser;
    private String assetOwner;
    private String assetSecurityManager;
    private OperationStatus operationStatus;
    private LocalDate introducedDate;
    private int confidentiality;
    private int integrity;
    private int availability;
    private String note;
    private String manufacturingCompany;
    private Ownership ownership;
    private Long purchaseCost;
    private LocalDate purchaseDate;
    private Long usefulLife;
    private DepreciationMethod depreciationMethod;
    private String warrantyDetails;
    private String attachment;
    private String purchaseSource;
    private String contactInformation;
    private Boolean disposalStatus;
    private Boolean demandStatus; // requestStatus 아님
    private Approval approval;
    private Boolean demandCheck;
    private LocalDate createDate;
    private UseState useState;
    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

}
