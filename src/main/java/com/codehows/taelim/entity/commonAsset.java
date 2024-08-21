package com.codehows.taelim.entity;

import com.codehows.taelim.constant.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "commonAsset")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class commonAsset {

    @Id
    @Column(name = "assetNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetNo;

    private assetClassification assetClassification;
    private assetBasis assetBasis;
    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;
    private department department;
    private assetLocation assetLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetUser")
    private member assetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetOwner")
    private member assetOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetSecurityManager")
    private member assetSecurityManager;

    private operationStatus operationStatus;
    private LocalDate introducedDate;
    private int confidentiality;
    private int integrity;
    private int availability;
    private String note;
    private String manufacturingCompany;
    private ownership ownership;
    private Long purchaseCost;
    private LocalDate purchaseDate;
    private Long usefulLife;
    private depreciationMethod depreciationMethod;
    private String warrantyDetails;
    private String attachment;
    private String purchaseSource;
    private String contactInformation;
    private String QRInformation;
    private Boolean disposalStatus;
    private Boolean requestStatus;
    private approval approval;
    private Boolean demandCheck;
    private LocalDate createDate;
    private useState useState;
    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

}
