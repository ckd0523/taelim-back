package com.codehows.taelim.dto;


import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetUpdateDto {

    private Long assetNo;

    private AssetClassification assetClassification;
    private AssetBasis assetBasis;
    private String assetCode;
    private String assetName;
    private String purpose;
    private Long quantity;
    private Department department;
    private AssetLocation assetLocation;
    private Member assetUser; // 이부분 나중에 고쳐야함
    private Member assetOwner; // 이부분 나중에 고쳐야함
    private Member assetSecurityManager; // 이부분 나중에 고쳐야함
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

    private Approval approval;
    private Boolean demandCheck;
    private LocalDate createDate;
    private UseState useState;
    private String acquisitionRoute;
    private LocalDate maintenancePeriod;

    // 정보 보호 시스템
    // Application Program
    private String serviceScope;
    private String os;
    private String relatedDB;
    private String ip;
    private Long screenNumber;
    // software
    private String serverId;
    private String serverPassword;
    private String companyManager;
    // 전자정보

    private String system;
    private String dbtype;
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
    private Long numberOfPorts;
    private String supportedProtocols;
    private String firmwareVersion;
    private Long networkSpeed;
    // 단말기
    private String productSerialNumber;
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
    private String furnitureSize;
    // 기타
    private String otherDescription;
    private String usageFrequency;

    // updateHistroyDto 관련부분
    private String updateReason;
    private String updateDetail;
    private String updateBy;
    private LocalDate updateDate;

    private List<FileDto> files;

//    public CommonAsset toEntity() {
//
//        return CommonAsset.builder()
//                .assetNo(assetNo)
//                .assetClassification(assetClassification)
//                .assetBasis(assetBasis)
//                .assetCode(assetCode)
//                .assetName(assetName)
//                .purpose(purpose)
//                .quantity(quantity)
//                .department(department)
//                .assetLocation(assetLocation)
//                .operationStatus(operationStatus)
//                .introducedDate(introducedDate)
//                .confidentiality(confidentiality)
//                .integrity(integrity)
//                .availability(availability)
//                .note(note)
//                .manufacturingCompany(manufacturingCompany)
//                .ownership(ownership)
//                .purchaseCost(purchaseCost)
//                .purchaseDate(purchaseDate)
//                .usefulLife(usefulLife)
//                .depreciationMethod(depreciationMethod)
//                .warrantyDetails(warrantyDetails)
//                .attachment(attachment)
//                .purchaseSource(purchaseSource)
//                .contactInformation(contactInformation)
//                .disposalStatus(disposalStatus)
//                .demandStatus(demandStatus)
//                .approval(approval)
//                .demandCheck(demandCheck)
//                .createDate(createDate)
//                .useState(useState)
//                .acquisitionRoute(acquisitionRoute)
//                .maintenancePeriod(maintenancePeriod)
//                .build();
//    }

    public InformationProtectionSystem toInformationProtectionSystem() {
        return InformationProtectionSystem.builder()
                .serviceScope(serviceScope)
                .build();
    }

    public ApplicationProgram toApplication() {
        return ApplicationProgram.builder()
                .serviceScope(serviceScope)
                .os(os)
                .relatedDB(relatedDB)
                .ip(ip)
                .screenNumber(screenNumber)
                .build();

    }

    public Software toSoftware() {
        return Software.builder()
                .ip(ip)
                .serverId(serverId)
                .serverPassword(serverPassword)
                .companyManager(companyManager)
                .os(os)
                .build();
    }

    public ElectronicInformation toElectronicInformation() {
        return ElectronicInformation.builder()
                .os(os)
                .system(system)
                .dbtype(dbtype)
                .build();
    }

    public Document toDocumnet() {
        return Document.builder()
                .documentGrade(documentGrade)
                .documentType(documentType)
                .documentLink(documentLink)
                .build();
    }


    public PatentsAndTrademarks toPatentsAndTrademarks(){
        return PatentsAndTrademarks.builder()
                .applicationDate(applicationDate)
                .registrationDate(registrationDate)
                .expirationDate(expirationDate)
                .patentTrademarkStatus(patentTrademarkStatus)
                .countryApplication(countryApplication)
                .patentClassification(patentClassification)
                .patentItem(patentItem)
                .applicationNo(applicationNo)
                .inventor(inventor)
                .assignee(assignee)
                .build();
    }


    public ItSystemEquipment toItSystemEquipment(){
        return ItSystemEquipment.builder()
                .equipmentType(equipmentType)
                .powerSupply(powerSupply)
                .coolingSystem(coolingSystem)
                .interfacePorts(interfacePorts)
                .formFactor(formFactor)
                .expansionSlots(expansionSlots)
                .graphicsCard(graphicsCard)
                .portConfiguration(portConfiguration)
                .monitorIncluded(monitorIncluded)
                .build();

    }

    public ItNetworkEquipment toItNetworkEquipment() {
        return ItNetworkEquipment.builder()
                .equipmentType(equipmentType)
                .numberOfPorts(numberOfPorts)
                .supportedProtocols(supportedProtocols)
                .firmwareVersion(firmwareVersion)
                .networkSpeed(networkSpeed)
                .serviceScope(serviceScope)
                .build();

    }

    public Terminal toTerminal() {
        return Terminal.builder()
                .ip(ip)
                .os(os)
                .securityControl(securityControl)
                .kaitsKeeper(kaitsKeeper)
                .V3OfficeSecurity(V3OfficeSecurity)
                .appCheckPro(appCheckPro)
                .tgate(tgate)
                .build();
    }

    public Furniture toFurniture() {
        return Furniture.builder()
                .furnitureSize(furnitureSize)
                .build();

    }

    public Devices toDevices() {
        return Devices.builder()
                .deviceType(deviceType)
                .modelNumber(modelNumber)
                .connectionType(connectionType)
                .powerSpecifications(powerSpecifications)
                .build();

    }

    public Car toCar() {
        return Car.builder()
                .displacement(displacement)
                .doorsCount(doorsCount)
                .engineType(engineType)
                .carType(carType)
                .identificationNo(identificationNo)
                .carColor(carColor)
                .modelYear(modelYear)
                .build();

    }

    public OtherAssets toOtherAssets() {
        return OtherAssets.builder()
                .otherDescription(otherDescription)
                .usageFrequency(usageFrequency)
                .build();
    }
}
