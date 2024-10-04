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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetUser")
    @JsonIgnore
    private Member assetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetOwner")
    @JsonIgnore
    private Member assetOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSecurityManager")
    @JsonIgnore
    private Member assetSecurityManager;
    
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
    private Boolean disposalStatus;
    private Boolean demandStatus; // requestStatus 아님

    @Enumerated(EnumType.STRING)
    private Approval approval;

    private Boolean demandCheck;
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private UseState useState;

    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

}
