package com.codehows.taelim.dto;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.InformationProtectionSystem;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.repository.MemberRepository;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDto {
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
    private String QRInformation;
    private Boolean disposalStatus;
    private Boolean demandStatus;

    private Boolean requestStatus;
    private Approval approval;
    private Boolean demandCheck;
    private Boolean demandStatus;
    private LocalDate createDate;
    private UseState useState;
    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

    private String serviceScope;
    private String OS;
    private String relatedDB;
    private String IP;
    private Long screenNumber;

    private String serverId;
    private String serverPassword;
    private String companyManager;

    private String system;
    private String DBType;

    private DocumentGrade documentGrade;
    private DocumentType documentType;
    private String documentLink;

    private LocalDate applicationDate;
    private LocalDate registrationDate;
    private LocalDate expirationDate;
    private PatentTrademarkStatus patentTrademarkStatus;
    private CountryApplication countryApplication;
    private PatentClassification patentClassification;
    private PatentItem patentItem;
    private String applicationNo;
    private String inventor;
    private String assignee;
    private String relatedDocuments;

    private String equipmentType;
    private Long rackUnit;
    private String powerSupply;
    private String coolingSystem;
    private String interfacePorts;
    private String formFactor;
    private Long expansionSlots;
    private String graphicsCard;
    private String portConfiguration;
    private Boolean monitorIncluded;

    private Long numberOfPorts;
    private String supportedProtocols;
    private String firmwareVersion;
    private Long networkSpeed;

    private String productSerialNumber;
    private SecurityControl securityControl;
    private LocalDate kaitsKeeper;
    private LocalDate V3OfficeSecurity;
    private LocalDate appCheckPro;
    private LocalDate tgate;

    private String deviceType;
    private String modelNumber;
    private String connectionType;
    private String powerSpecifications;

    private Long displacement;
    private Long doorsCount;
    private EngineType engineType;
    private CarType carType;
    private String identificationNo;
    private String carColor;
    private Long modelYear;

    private String furnitureSize;

    private String otherDescription;
    private String usageFrequency;

    public CommonAsset toEntity() {

        return CommonAsset.builder()
                .assetNo(assetNo)
                .assetClassification(assetClassification)
                .assetBasis(assetBasis)
                .assetCode(assetCode)
                .assetName(assetName)
                .purpose(purpose)
                .quantity(quantity)
                .department(department)
                .assetLocation(assetLocation)
                .operationStatus(operationStatus)
                .introducedDate(introducedDate)
                .confidentiality(confidentiality)
                .integrity(integrity)
                .availability(availability)
                .note(note)
                .manufacturingCompany(manufacturingCompany)
                .ownership(ownership)
                .purchaseDate(purchaseDate)
                .usefulLife(usefulLife)
                .depreciationMethod(depreciationMethod)
                .warrantyDetails(warrantyDetails)
                .attachment(attachment)
                .purchaseSource(purchaseSource)
                .contactInformation(contactInformation)
                .QRInformation(QRInformation)
                .disposalStatus(disposalStatus)
                .demandStatus(demandStatus)
                .approval(approval)
                .demandCheck(demandCheck)
                .createDate(createDate)
                .useState(useState)
                .acquisitionRoute(acquisitionRoute)
                .maintenancePeriod(maintenancePeriod)
                .build();
    }

    public InformationProtectionSystem toEntity2() {
        return InformationProtectionSystem.builder()
                .serviceScope(serviceScope)
                .build();
    }

}
