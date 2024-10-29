package com.codehows.taelim.entity;

import com.codehows.taelim.constant.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "commonAsset")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommonAsset {

    @Id
    @Column(name = "assetNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetNo;

    @Enumerated(EnumType.STRING)
    private AssetClassification assetClassification;

    @Enumerated(EnumType.STRING)
    private AssetBasis assetBasis;

    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Enumerated(EnumType.STRING)
    private AssetLocation assetLocation;


    private String assetUser;


    private String assetOwner;


    private String assetSecurityManager;
    
    @Enumerated(EnumType.STRING)
    private OperationStatus operationStatus;

    private LocalDate introducedDate;
    private int confidentiality;
    private int integrity;
    private int availability;
    private String note;
    private String manufacturingCompany;

    @Enumerated(EnumType.STRING)
    private Ownership ownership;

    private Long purchaseCost;
    private LocalDate purchaseDate;
    private Long usefulLife;

    @Enumerated(EnumType.STRING)
    private DepreciationMethod depreciationMethod;

    private String warrantyDetails;
    private String attachment;
    private String purchaseSource;
    private String contactInformation;

    @Enumerated(EnumType.STRING)
    private UseStated useStated;

    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

    //제품 시리얼 No. 공통으로
    private String productSerialNumber;

    private Boolean disposalStatus;

    private Boolean demandStatus; // requestStatus 아님

    @Enumerated(EnumType.STRING)
    private Approval approval;

    private Boolean demandCheck;
    private LocalDate createDate;


}
