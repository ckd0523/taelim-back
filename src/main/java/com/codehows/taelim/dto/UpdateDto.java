package com.codehows.taelim.dto;


import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UpdateDto {

    private Long assetNo;
    private Member assetUser;
    private Member assetOwner;
    private Member assetSecurityManager;
    private AssetClassification assetClassification;
    private AssetBasis assetBasis;
    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;
    private Department department;
    private AssetLocation assetLocation;
    private OperationStatus operationStatus;
    private LocalDate introducedDate;
    private Integer confidentiality;
    private Integer integrity;
    private Integer availability;
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
    private Approval approval;
    private LocalDate createDate;

    // 정보 보호 시스템
    //private String serviceScope; application Program겹침
    // Application Program
    private String serviceScope;
    private String os;
    private String relatedDB;
    private String ip;
    private Long screenNumber;
    // software
    //private String IP; application Program겹침
    private String serverId;
    private String serverPassword;
    private String companyManager;
    //private String OS; application Program겹침
    // 전자정보
    //private String OS; application Program겹침
    private String system;
    private String DBType;
    // 문서
    private DocumentGrade documentGrade;
    private DocumentType documentType;
    private String documentLink;
    // 특허 및 상표
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
    // it 장비(시스템)
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
    // it 장비 네트워크
    //rivate String equipmentType; it 장비 (시스템) 겹침
    private Long numberOfPorts;
    private String supportedProtocols;
    private String firmwareVersion;
    private Long networkSpeed;
    //private String serviceScope;application Program겹침
    // 단말기
    //private String IP; application Program겹침
    private String productSerialNumber;
    //private String OS; application Program겹침
    private SecurityControl securityControl;
    private LocalDate kaitsKeeper;
    private LocalDate V3OfficeSecurity;
    private LocalDate appCheckPro;
    private LocalDate tgate;
    // 기기
    private String deviceType;
    private String modelNumber;
    private String connectionType;
    private String powerSpecifications;
    // 차량
    private Long displacement;
    private Long doorsCount;
    private EngineType engineType;
    private CarType carType;
    private String identificationNo;
    private String carColor;
    private Long modelYear;
    // 가구
    private String funitureSize;
    // 기타
    private String otherDescription;
    private String usageFrequency;

}
