package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DataInitializerService {

    private final MemberRepository memberRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final SoftwareRepository softwareRepository;
    private final CarRepository carRepository;
    private final DevicesRepository devicesRepository;
    private final DocumentRepository documentRepository;
    private final TerminalRepository terminalRepository;
    private final FurnitureRepository furnitureRepository;
    private final OtherAssetsRepository otherAssetsRepository;
    private final ItSystemEquipmentRepository itSystemEquipmentRepository;
    private final ApplicationProgramRepository applicationProgramRepository;
    private final ItNetworkEquipmentRepository itNetworkEquipmentRepository;
    private final ElectronicInformationRepository electronicInformationRepository;
    private final PatentsAndTrademarksRepository patentsAndTrademarksRepository;
    private final InformationProtectionSystemRepository informationProtectionSystemRepository;
    private final RegisterService registerService;
    @Transactional
    public void insertDummyData() {
        // Member 데이터 삽입
        for (int i = 1; i <= 40; i++) {
            Member member = new Member();
            member.setEmail("user" + i + "@example.com");
            member.setPassword("password" + i);
            member.setUName("User Name " + i);
            member.setRole(i % 10 == 0 ? Role.ADMIN : (i % 2 == 0 ? Role.ASSET_MANAGER : Role.USER));
            memberRepository.save(member);
        }

        // CommonAsset 첫번째 데이터 삽입
        for (int i = 1; i <= 40; i++) {

            Member member = new Member();
            member.setEmail("user" + i + "@example.com");
            member.setPassword("password" + i);
            member.setUName("User Name " + i);
            member.setRole(Role.USER);

            CommonAsset asset = new CommonAsset();
            if (i <= 5) {
                asset.setAssetClassification(AssetClassification.SOFTWARE);
            }else {
                asset.setAssetClassification(AssetClassification.FURNITURE);
            }
            asset.setAssetBasis(AssetBasis.COMMON);
            asset.setAssetCode(String.format("ASSET%03d", i));
            asset.setAssetName("Asset " + i);
            asset.setPurpose("Test Purpose");
            asset.setQuantity(1L);
            asset.setDepartment(Department.IT_DEPARTMENT);
            asset.setAssetLocation(AssetLocation.MAIN_1F);
            asset.setAssetUser(member);
            asset.setAssetOwner(member);
            asset.setAssetSecurityManager(member);
            asset.setOperationStatus(OperationStatus.OPERATING);
            asset.setIntroducedDate(LocalDate.now());
            asset.setConfidentiality(1);
            asset.setIntegrity(1);
            asset.setAvailability(1);
            asset.setNote("Test Note");
            asset.setManufacturingCompany("Test Manufacturing Company");
            asset.setOwnership(Ownership.OWNED);
            asset.setPurchaseCost(5000L);
            asset.setPurchaseDate(LocalDate.now());
            asset.setUsefulLife(5L);
            asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
            asset.setWarrantyDetails("Test Warranty Details");
            asset.setAttachment("Test Attachment");
            asset.setPurchaseSource("Test Purchase Source");
            asset.setContactInformation("010-0000-0000");
            asset.setDisposalStatus(Boolean.FALSE);
            asset.setDemandStatus(Boolean.FALSE);
            asset.setApproval(Approval.APPROVE);
            asset.setDemandCheck(Boolean.FALSE);
            asset.setCreateDate(LocalDate.now());
            asset.setUseState(UseState.IN_USE);
            asset.setAcquisitionRoute("Test Acquisition Route");
            asset.setMaintenancePeriod(LocalDate.now());
            // 기타 필드 설정
            commonAssetRepository.save(asset);
            if(i<=5) {
                Software software = new Software();
                software.setAssetNo(asset);
                software.setIp("192.168.1." + i);
                software.setServerId("server" + String.format("%02d", i));
                software.setServerPassword("pass" + i);
                software.setCompanyManager("Manager " + i);
                software.setOs("Windows Server 2022");
                softwareRepository.save(software);
            } else {
                Furniture furniture = new Furniture();
                furniture.setAssetNo(asset);
                furniture.setFurnitureSize("500");
                furnitureRepository.save(furniture);
            }
        }

//        @Transactional
//        public void insertDummyData() {
//            // Member 데이터 삽입
//            for (int i = 1; i <= 195; i++) {
//                Member member = new Member();
//                member.setEmail("user" + i + "@example.com");
//                member.setPassword("password" + i);
//                member.setUName("User Name " + i);
//                member.setRole(i % 10 == 0 ? Role.ADMIN : (i % 2 == 0 ? Role.ASSET_MANAGER : Role.USER));
//                memberRepository.save(member);
//            }
//            // 자산 분류 항목
//            AssetClassification[] classifications = AssetClassification.values();
//            int classificationIndex = 0;  // 분류 인덱스
//            int assetCountPerClassification = 15;  // 각 분류당 15개씩 생
//
//            // CommonAsset 첫번째 데이터 삽입
//            for (int i = 1; i <= 195; i++) {
//
//                Member member = new Member();
//                member.setEmail("user" + i + "@example.com");
//                member.setPassword("password" + i);
//                member.setUName("User Name " + i);
//                member.setRole(Role.USER);
//
//                CommonAsset asset = new CommonAsset();
//
//                // 자산 분류는 총 13개, 이를 15개씩 순환하며 할당
//                AssetClassification classification = classifications[(i - 1) / 15];
//                String assetCode = registerService.generateAssetCode(classification);
//                asset.setAssetCode(assetCode);
//
//                if (i <= 5) {
//                    asset.setAssetClassification(AssetClassification.SOFTWARE);
//                }else {
//                    asset.setAssetClassification(AssetClassification.FURNITURE);
//                }
//                asset.setAssetBasis(AssetBasis.COMMON);
//
//                asset.setAssetName("Asset " + i);
//                asset.setPurpose("Test Purpose");
//                asset.setQuantity(1L);
//                asset.setDepartment(Department.IT_DEPARTMENT);
//                asset.setAssetLocation(AssetLocation.MAIN_1F);
//                asset.setAssetUser(member);
//                asset.setAssetOwner(member);
//                asset.setAssetSecurityManager(member);
//                asset.setOperationStatus(OperationStatus.OPERATING);
//                asset.setIntroducedDate(LocalDate.now());
//                asset.setConfidentiality(1);
//                asset.setIntegrity(1);
//                asset.setAvailability(1);
//                asset.setNote("Test Note");
//                asset.setManufacturingCompany("Test Manufacturing Company");
//                asset.setOwnership(Ownership.OWNED);
//                asset.setPurchaseCost(5000L);
//                asset.setPurchaseDate(LocalDate.now());
//                asset.setUsefulLife(5L);
//                asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
//                asset.setWarrantyDetails("Test Warranty Details");
//                asset.setAttachment("Test Attachment");
//                asset.setPurchaseSource("Test Purchase Source");
//                asset.setContactInformation("010-0000-0000");
//                asset.setDisposalStatus(Boolean.FALSE);
//                asset.setDemandStatus(Boolean.FALSE);
//                asset.setApproval(Approval.APPROVE);
//                asset.setDemandCheck(Boolean.FALSE);
//                asset.setCreateDate(LocalDate.now());
//                asset.setUseState(UseState.IN_USE);
//                asset.setAcquisitionRoute("Test Acquisition Route");
//                asset.setMaintenancePeriod(LocalDate.now());
//                // 기타 필드 설정
//                commonAssetRepository.save(asset);
//
//                // 자산 분류에 따른 세부 엔티티 처리
//                CommonAsset commonAsset1 = commonAssetRepository.findTopByOrderByAssetNoDesc();
//
//                switch (classification) {
//                    case INFORMATION_PROTECTION_SYSTEM -> {
//                        InformationProtectionSystem informationProtectionSystem = new InformationProtectionSystem();
//                        informationProtectionSystem.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        informationProtectionSystem.setServiceScope("serviceScope " + i);  // 더미 데이터 반영
//                        informationProtectionSystemRepository.save(informationProtectionSystem);
//                    }
//                    case APPLICATION_PROGRAM -> {
//                        ApplicationProgram applicationProgram = new ApplicationProgram();
//                        applicationProgram.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        applicationProgram.setServiceScope("Application Program " + i);  // 더미 데이터 반영
//                        applicationProgram.setOs("Os " + i);  // 더미 데이터 반영
//                        applicationProgram.setRelatedDB("RelatedDB" + i);
//                        applicationProgram.setIp("Ip" + i);
//                        //applicationProgram.setScreenNumber(i);  // 더미 데이터 반영
//                        applicationProgramRepository.save(applicationProgram);
//                    }
//                    case SOFTWARE -> {
//                        Software software = new Software();
//                        software.setIp("192.168.1." + i);  // 더미 데이터 반영
//                        software.setServerId("ServerID_" + i);  // 더미 데이터 반영
//                        software.setServerPassword("password_" + i);  // 더미 데이터 반영
//                        software.setCompanyManager("Manager " + i);  // 더미 데이터 반영
//                        software.setOs("Windows Server 2022");  // 더미 데이터 반영
//                        softwareRepository.save(software);
//                    }
//                    case ELECTRONIC_INFORMATION -> {
//                        ElectronicInformation electronicInformation = new ElectronicInformation();
//                        electronicInformation.setOs("Linux");  // 더미 데이터 반영
//                        electronicInformation.setSystem("System_" + i);  // 더미 데이터 반영
//                        electronicInformation.setDbtype("Database Type " + i);  // 더미 데이터 반영
//                        electronicInformationRepository.save(electronicInformation);
//                    }
//                    case DOCUMENT -> {
//                        Document document = new Document();
//                        document.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        document.setDocumentGrade("Grade A");  // 더미 데이터 반영
//                        document.setDocumentType("Document Type " + i);  // 더미 데이터 반영
//                        document.setDocumentLink("http://documentlink" + i + ".com");  // 더미 데이터 반영
//                        documentRepository.save(document);
//                    }
//                    case PATENTS_AND_TRADEMARKS -> {
//                        PatentsAndTrademarks patentsAndTrademarks = new PatentsAndTrademarks();
//                        patentsAndTrademarks.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        patentsAndTrademarks.setApplicationDate(LocalDate.of(2020, i % 12 + 1, i % 28 + 1));  // 더미 데이터 반영
//                        patentsAndTrademarks.setRegistrationDate(LocalDate.of(2021, i % 12 + 1, i % 28 + 1));  // 더미 데이터 반영
//                        patentsAndTrademarks.setExpirationDate(LocalDate.of(2030, i % 12 + 1, i % 28 + 1));  // 더미 데이터 반영
//                        patentsAndTrademarks.setPatentTrademarkStatus("Active");  // 더미 데이터 반영
//                        patentsAndTrademarks.setCountryApplication("Country " + i);  // 더미 데이터 반영
//                        patentsAndTrademarks.setPatentClassification("Classification " + i);  // 더미 데이터 반영
//                        patentsAndTrademarks.setPatentItem("Patent Item " + i);  // 더미 데이터 반영
//                        patentsAndTrademarks.setApplicationNo("AppNo_" + i);  // 더미 데이터 반영
//                        patentsAndTrademarks.setInventor("Inventor " + i);  // 더미 데이터 반영
//                        patentsAndTrademarks.setAssignee("Assignee " + i);  // 더미 데이터 반영
//                        patentsAndTrademarks.setRelatedDocuments("Related Docs " + i);  // 더미 데이터 반영
//                        patentsAndTrademarksRepository.save(patentsAndTrademarks);
//                    }
//                    case ITSYSTEM_EQUIPMENT -> {
//                        ItSystemEquipment itSystemEquipment = new ItSystemEquipment();
//                        itSystemEquipment.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        itSystemEquipment.setEquipmentType("Type " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setRackUnit(i);  // 더미 데이터 반영
//                        itSystemEquipment.setPowerSupply("Power Supply " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setCoolingSystem("Cooling System " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setInterfacePorts("Port " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setFormFactor("Form Factor " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setExpansionSlots(i);  // 더미 데이터 반영
//                        itSystemEquipment.setGraphicsCard("Graphics Card " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setPortConfiguration("Port Configuration " + i);  // 더미 데이터 반영
//                        itSystemEquipment.setMonitorIncluded(i % 2 == 0);  // 더미 데이터 반영
//                        itSystemEquipmentRepository.save(itSystemEquipment);
//                    }
//                    case ITNETWORK_EQUIPMENT -> {
//                        ItNetworkEquipment itNetworkEquipment = new ItNetworkEquipment();
//                        itNetworkEquipment.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        itNetworkEquipment.setEquipmentType("Network Equipment Type " + i);  // 더미 데이터 반영
//                        itNetworkEquipment.setNumberOfPorts(i);  // 더미 데이터 반영
//                        itNetworkEquipment.setSupportedProtocols("Protocol " + i);  // 더미 데이터 반영
//                        itNetworkEquipment.setFirmwareVersion("v1." + i);  // 더미 데이터 반영
//                        itNetworkEquipment.setNetworkSpeed("1Gbps");  // 더미 데이터 반영
//                        itNetworkEquipment.setServiceScope("Service Scope " + i);  // 더미 데이터 반영
//                        itNetworkEquipmentRepository.save(itNetworkEquipment);
//                    }
//                    case TERMINAL -> {
//                        Terminal terminal = new Terminal();
//                        terminal.setIp("192.168.1." + i);  // 더미 데이터 반영
//                        terminal.setProductSerialNumber("Serial" + i);  // 더미 데이터 반영
//                        terminal.setOs("Terminal OS " + i);  // 더미 데이터 반영
//                        terminal.setSecurityControl("Security Control " + i);  // 더미 데이터 반영
//                        terminal.setKaitsKeeper(i % 2 == 0);  // 더미 데이터 반영
//                        terminal.setV3OfficeSecurity(i % 2 == 1);  // 더미 데이터 반영
//                        terminal.setAppCheckPro(i % 2 == 0);  // 더미 데이터 반영
//                        terminal.setTgate(i % 2 == 1);  // 더미 데이터 반영
//                        terminalRepository.save(terminal);
//                    }
//                    case FURNITURE -> {
//                        Furniture furniture = new Furniture();
//                        furniture.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        furniture.setFurnitureSize("Size " + i);  // 더미 데이터 반영
//                        furnitureRepository.save(furniture);
//                    }
//                    case DEVICES -> {
//                        Devices devices = new Devices();
//                        devices.setDeviceType("Device Type " + i);  // 더미 데이터 반영
//                        devices.setModelNumber("Model " + i);  // 더미 데이터 반영
//                        devices.setConnectionType("Wired");  // 더미 데이터 반영
//                        devices.setPowerSpecifications("5V, 2A");  // 더미 데이터 반영
//                        devicesRepository.save(devices);
//                    }
//                    case CAR -> {
//                        Car car = new Car();
//                        car.setAssetNo(commonAsset1);  // 자산 번호 설정
//                        car.setCarModel("Car Model " + i);  // 더미 데이터 반영
//                        car.setLicensePlate("License" + String.format("%02d", i));  // 더미 데이터 반영
//                        car.setDisplacement("2000cc");  // 더미 데이터 반영
//                        car.setDoorsCount(4);  // 더미 데이터 반영
//                        car.setEngineType("V6");  // 더미 데이터 반영
//                        car.setCarType("Sedan");  // 더미 데이터 반영
//                        car.setIdentificationNo("ID" + i);  // 더미 데이터 반영
//                        carRepository.save(car);
//                    }
//                    case OTHERASSETS -> {
//                        OtherAssets otherAssets = new OtherAssets();
//                        otherAssets.setAssetNo(commonAsset1);
//                        otherAssets.setOtherDescription("otherDescription " + i);
//                        otherAssets.setUsageFrequency("usageFrequency" + i);
//                        otherAssetsRepository.save(otherAssets);
//                    }
//                }
//
//                // 15개씩 생성했으면 다음 분류로 이동
//                if (i % assetCountPerClassification == 0) {
//                    classificationIndex++;
//                }
//            }

        //수정
//        for (int i = 1; i <= 10; i++) {
//            if(i%3==0) {
//                Member member = new Member();
//                member.setEmail("user" + i + "@example.com");
//                member.setPassword("password" + i);
//                member.setUName("User Name " + i);
//                member.setRole(Role.USER);
//
//                CommonAsset asset = new CommonAsset();
//                asset.setAssetClassification(AssetClassification.FURNITURE);
//                asset.setAssetBasis(AssetBasis.COMMON);
//                asset.setAssetCode(String.format("ASSET%03d", i));
//                asset.setAssetName("Asset " + i);
//                asset.setPurpose("Test Purpose");
//                asset.setQuantity(1L);
//                asset.setDepartment(Department.IT_DEPARTMENT);
//                asset.setAssetLocation(AssetLocation.MAIN_1F);
//                asset.setAssetUser(member);
//                asset.setAssetOwner(member);
//                asset.setAssetSecurityManager(member);
//                asset.setOperationStatus(OperationStatus.OPERATING);
//                asset.setIntroducedDate(LocalDate.now());
//                asset.setConfidentiality(1);
//                asset.setIntegrity(1);
//                asset.setAvailability(1);
//                asset.setNote("Test Note");
//                asset.setManufacturingCompany("Test Manufacturing Company");
//                asset.setOwnership(Ownership.OWNED);
//                asset.setPurchaseCost(5000L);
//                asset.setPurchaseDate(LocalDate.now());
//                asset.setUsefulLife(5L);
//                asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
//                asset.setWarrantyDetails("Test Warranty Details");
//                asset.setAttachment("Test Attachment");
//                asset.setPurchaseSource("Test Purchase Source");
//                asset.setContactInformation("010-0000-0000");
//                asset.setDisposalStatus(Boolean.FALSE);
//
//                asset.setDemandStatus(Boolean.TRUE);
//                if (i < 5) {
//                    asset.setApproval(Approval.APPROVE);
//                } else {
//                    asset.setApproval(Approval.REFUSAL);
//                }
//
//                asset.setDemandCheck(Boolean.TRUE);
//                asset.setCreateDate(LocalDate.now());
//                asset.setUseState(UseState.IN_USE);
//                asset.setAcquisitionRoute("Test Acquisition Route");
//                asset.setMaintenancePeriod(LocalDate.now());
//                // 기타 필드 설정
//                commonAssetRepository.save(asset);
//                if(i<=5) {
//                    Software software = new Software();
//                        software.setAssetNo(asset);
//                        software.setIp("192.168.1." + i);
//                        software.setServerId("server" + String.format("%02d", i));
//                        software.setServerPassword("pass" + i);
//                        software.setCompanyManager("Manager " + i);
//                        software.setOs("Windows Server 2022");
//                        softwareRepository.save(software);
//                    } else {
//                    Furniture furniture = new Furniture();
//                        furniture.setAssetNo(asset);
//                        furniture.setFurnitureSize("500");
//                        furnitureRepository.save(furniture);
//                }
//            }
//        }

        //폐기
//        for (int i = 1; i <= 10; i++) {
//            if(i%5==0) {
//                Member member = new Member();
//                member.setEmail("user" + i + "@example.com");
//                member.setPassword("password" + i);
//                member.setUName("User Name " + i);
//                member.setRole(Role.USER);
//
//                CommonAsset asset = new CommonAsset();
//                asset.setAssetClassification(AssetClassification.FURNITURE);
//                asset.setAssetBasis(AssetBasis.COMMON);
//                asset.setAssetCode(String.format("ASSET%03d", i));
//                asset.setAssetName("Asset " + i);
//                asset.setPurpose("Test Purpose");
//                asset.setQuantity(1L);
//                asset.setDepartment(Department.IT_DEPARTMENT);
//                asset.setAssetLocation(AssetLocation.MAIN_1F);
//                asset.setAssetUser(member);
//                asset.setAssetOwner(member);
//                asset.setAssetSecurityManager(member);
//                asset.setOperationStatus(OperationStatus.OPERATING);
//                asset.setIntroducedDate(LocalDate.now());
//                asset.setConfidentiality(1);
//                asset.setIntegrity(1);
//                asset.setAvailability(1);
//                asset.setNote("Test Note");
//                asset.setManufacturingCompany("Test Manufacturing Company");
//                asset.setOwnership(Ownership.OWNED);
//                asset.setPurchaseCost(5000L);
//                asset.setPurchaseDate(LocalDate.now());
//                asset.setUsefulLife(5L);
//                asset.setDepreciationMethod(DepreciationMethod.FIXED_RATE);
//                asset.setWarrantyDetails("Test Warranty Details");
//                asset.setAttachment("Test Attachment");
//                asset.setPurchaseSource("Test Purchase Source");
//                asset.setContactInformation("010-0000-0000");
//                asset.setQRInformation("Test QR Information");
//                asset.setDisposalStatus(Boolean.TRUE);
//                asset.setRequestStatus(Boolean.TRUE);
//                if (i < 8) {
//                    asset.setApproval(Approval.APPROVE);
//                    asset.setUseState(UseState.RETIRED_DISCARDED);
//                } else {
//                    asset.setApproval(Approval.REFUSAL);
//                    asset.setUseState(UseState.IN_USE);
//                }
//                asset.setDemandCheck(Boolean.TRUE);
//                asset.setCreateDate(LocalDate.now());
//                asset.setAcquisitionRoute("Test Acquisition Route");
//                asset.setMaintenancePeriod(LocalDate.now());
//                // 기타 필드 설정
//                commonAssetRepository.save(asset);
//            }
//        }

    }
}



//if(i<=50) {
//Software software = new Software();
//                software.setAssetNo(asset);
//                software.setIP("192.168.1." + i);
//                software.setServerId("server" + String.format("%02d", i));
//        software.setServerPassword("pass" + i);
//                software.setCompanyManager("Manager " + i);
//                software.setOS("Windows Server 2022");
//                softwareRepository.save(software);
//            } else {
//Furniture furniture = new Furniture();
//                furniture.setAssetNo(asset);
//                furniture.setFurnitureSize("500");
//            }
