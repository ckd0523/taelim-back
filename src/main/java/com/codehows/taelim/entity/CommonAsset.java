package com.codehows.taelim.entity;

import com.codehows.taelim.constant.*;
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

    private AssetClassification assetClassification;
    private AssetBasis assetBasis;
    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;
    private Department department;
    private AssetLocation assetLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetUser")
    private Member assetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetOwner")
    private Member assetOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSecurityManager")
    private Member assetSecurityManager;

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
    private String QRInformation;
    private Boolean disposalStatus;
    private Boolean requestStatus;
    private Approval approval;
    private Boolean demandCheck;
    private LocalDate createDate;
    private UseState useState;
    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

}
