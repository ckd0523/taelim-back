package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.UserDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Furniture;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.entity.Software;
import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
import com.codehows.taelim.secondRepository.TestMemberRepository;
import com.codehows.taelim.repository.*;
import com.codehows.taelim.security.PasswordHasher2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private final AmountSetRepository amountSetRepository;
    private final TestMemberRepository testMemberRepository;
    private final PasswordHasher2 passwordHasher2;
    private final UserService userService;
    private final AspNetUserRepository aspNetUserRepository;


        @Transactional
        public void insertDummyData() {
            //자산 기준 금액 설정 초기값 설정
            amountSetRepository.insertAmountSet(0L, 0L);
            // UserDto를 DB에서 가져오는 서비스 메서드
            // 자산 분류 항목
            AssetClassification[] classifications = AssetClassification.values();
            int classificationIndex = 0;  // 분류 인덱스
            int assetCountPerClassification = 15;  // 각 분류당 15개씩 생

            // AspNetUser 데이터를 DB에서 가져옴
            List<AspNetUser> userList = aspNetUserRepository.findAll();  // AspNetUser 엔티티를 모두 조회

            // 2024년 10월 1일부터 31일까지의 날짜 범위
            LocalDate startDate = LocalDate.of(2015, 10, 1);
            LocalDate endDate = LocalDate.of(2025, 12, 31);

            List<CommonAsset> assets = new ArrayList<>();
            String[] assetNames = {
                    "방화벽",
                    "침입 탐지 시스템",
                    "침입 방지 시스템",
                    "안티바이러스 소프트웨어",
                    "데이터 암호화 솔루션",
                    "VPN"
            };

            // 차량 모델 이름 배열
            String[] carModels = {
                    "현대 아반떼",
                    "기아 K5",
                    "삼성 SM6",
                    "현대 스타렉스",
                    "기아 봉고",
            };
            Random random = new Random();

            // CommonAsset 첫번째 데이터 삽입
            for (int i = 1; i <= 195; i++) {

                CommonAsset asset = new CommonAsset();
                // 자산 분류는 총 13개, 이를 15개씩 순환하며 할당
                AssetClassification classification = classifications[(i - 1) / 15];
                String assetCode = registerService.generateAssetCode(classification);
                asset.setAssetCode(assetCode);

                if (i <= 15) {
                    asset.setAssetClassification(AssetClassification.INFORMATION_PROTECTION_SYSTEM);
                    asset.setAssetName(assetNames[random.nextInt(assetNames.length)]);
                } else if (i <= 30) {
                    asset.setAssetClassification(AssetClassification.APPLICATION_PROGRAM);
                    asset.setAssetName("응용 프로그램 자산 " + (i + 1)); // 응용 프로그램 자산명
                } else if (i <= 45) {
                    asset.setAssetClassification(AssetClassification.SOFTWARE);
                    asset.setAssetName("소프트웨어 자산 " + (i + 1)); // 소프트웨어 자산명
                } else if (i <= 60) {
                    asset.setAssetClassification(AssetClassification.ELECTRONIC_INFORMATION);
                    asset.setAssetName("전자정보 자산 " + (i + 1)); // 전자정보 자산명
                } else if (i <= 75) {
                    asset.setAssetClassification(AssetClassification.DOCUMENT);
                    asset.setAssetName("문서 자산 " + (i + 1)); // 문서 자산명
                } else if (i <= 90) {
                    asset.setAssetClassification(AssetClassification.PATENTS_AND_TRADEMARKS);
                    asset.setAssetName("특허 및 상표 자산 " + (i + 1)); // 특허 및 상표 자산명
                } else if (i <= 105) {
                    asset.setAssetClassification(AssetClassification.ITSYSTEM_EQUIPMENT);
                    asset.setAssetName("IT 시스템 장비 자산 " + (i + 1)); // IT 시스템 장비 자산명
                } else if (i <= 120) {
                    asset.setAssetClassification(AssetClassification.ITNETWORK_EQUIPMENT);
                    asset.setAssetName("IT 네트워크 장비 자산 " + (i + 1)); // IT 네트워크 장비 자산명
                } else if (i <= 135) {
                    asset.setAssetClassification(AssetClassification.TERMINAL);
                    asset.setAssetName("단말기 자산 " + (i + 1)); // 단말기 자산명
                } else if (i <= 150) {
                    asset.setAssetClassification(AssetClassification.FURNITURE);
                    asset.setAssetName("가구 자산 " + (i + 1)); // 가구 자산명
                } else if (i <= 165) {
                    asset.setAssetClassification(AssetClassification.DEVICES);
                    asset.setAssetName("기기 자산 " + (i + 1)); // 기기 자산명
                } else if (i <= 180) {
                    asset.setAssetClassification(AssetClassification.CAR);
                    asset.setAssetName(carModels[random.nextInt(carModels.length)]); // 차량 이름 랜덤 선택
                } else if (i <= 195) {
                    asset.setAssetClassification(AssetClassification.OTHERASSETS);
                    asset.setAssetName("기타 자산 " + (i + 1)); // 기타 자산명
                }
                asset.setAssetBasis(AssetBasis.COMMON);

               //asset.setAssetName("Asset " + i);
                asset.setPurpose("Test Purpose");
                asset.setQuantity(1L);
                asset.setDepartment(Department.MANAGEMENT_PLANNING_OFFICE);
                // AssetLocation을 순환하여 설정
                AssetLocation assetLocation = AssetLocation.values()[i% AssetLocation.values().length];
                asset.setAssetLocation(assetLocation);
                // Department을 순환하여 설정
                Department department = Department.values()[i % Department.values().length];
                asset.setDepartment(department);

                // userList에서 데이터를 순환하여 할당 (예시로 i % userList.size() 사용)
                AspNetUser aspNetUser = userList.get(i % userList.size());
                asset.setAssetUser(aspNetUser.getId());
                asset.setAssetOwner(aspNetUser.getId());
                asset.setAssetSecurityManager(aspNetUser.getId());
                // OperationStatus을 순환하여 설정
                OperationStatus operationStatus = OperationStatus.values()[i % OperationStatus.values().length];
                asset.setOperationStatus(operationStatus);

                // 2024년 10월 1일부터 31일까지의 날짜 순환 설정
                LocalDate introducedDate = startDate.plusDays((i - 1) % 31); // 0부터 30까지 순환
                asset.setIntroducedDate(introducedDate);
                // 보안성 설정 (1~3 범위)
                asset.setConfidentiality(random.nextInt(3) + 1); // 1~3 랜덤 값
                asset.setIntegrity(random.nextInt(3) + 1);        // 1~3 랜덤 값
                asset.setAvailability(random.nextInt(3) + 1);     // 1~3 랜덤 값
                asset.setNote("Test Note");
                asset.setManufacturingCompany("Test Manufacturing Company");
                //Owenerhip
                Ownership ownership = Ownership.values()[i % Ownership.values().length];
                asset.setOwnership(ownership);
                // 랜덤으로 구매비용 및 잔존가치, 현재가치 설정
                long purchaseCost = 5000 + (random.nextInt(196001) / 1000) * 1000; // 5000에서 200000 사이의 랜덤 값 (천 단위)
                asset.setPurchaseCost(purchaseCost);

                // 랜덤한 구매 날짜 설정
                LocalDate purchaseDate = startDate.plusDays(random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1));
                asset.setPurchaseDate(purchaseDate);

                // 랜덤한 내용연수 설정 (1-10년 사이)
                long usefulLife = random.nextInt(10) + 1;
                asset.setUsefulLife(usefulLife);
                // 정액법 정률법
                DepreciationMethod depreciationMethod = DepreciationMethod.values()[i % DepreciationMethod.values().length];
                asset.setDepreciationMethod(depreciationMethod);
                asset.setWarrantyDetails("Test Warranty Details");
                asset.setAttachment("Test Attachment");
                asset.setPurchaseSource("Test Purchase Source");
                asset.setContactInformation("010-0000-0000");
                asset.setDisposalStatus(Boolean.FALSE);
                asset.setDemandStatus(Boolean.FALSE);
                asset.setApproval(Approval.APPROVE);
                asset.setDemandCheck(Boolean.FALSE);
                asset.setCreateDate(LocalDate.now());
                asset.setUseStated(UseStated.IN_USE);
                asset.setAcquisitionRoute("Test Acquisition Route");
                asset.setMaintenancePeriod(LocalDate.now());
                asset.setProductSerialNumber("Serial" + i);  // 더미 데이터 반영

                // 기타 필드 설정
                commonAssetRepository.save(asset);

                // 자산 분류에 따른 세부 엔티티 처리
                CommonAsset commonAsset1 = commonAssetRepository.findTopByOrderByAssetNoDesc();

                switch (classification) {
                    case INFORMATION_PROTECTION_SYSTEM -> {
                        InformationProtectionSystem informationProtectionSystem = new InformationProtectionSystem();
                        informationProtectionSystem.setAssetNo(commonAsset1);  // 자산 번호 설정
                        informationProtectionSystem.setServiceScope("serviceScope " + i);  // 더미 데이터 반영
                        informationProtectionSystemRepository.save(informationProtectionSystem);
                    }
                    case APPLICATION_PROGRAM -> {
                        ApplicationProgram applicationProgram = new ApplicationProgram();
                        applicationProgram.setAssetNo(commonAsset1);  // 자산 번호 설정
                        applicationProgram.setServiceScope("Application Program " + i);  // 더미 데이터 반영
                        applicationProgram.setOs("Os " + i);  // 더미 데이터 반영
                        applicationProgram.setRelatedDB("RelatedDB" + i);
                        applicationProgram.setIp("Ip" + i);
                        // int를 Long으로 변환하여 설정
                        applicationProgram.setScreenNumber(Long.valueOf(i));  // Long 타입으로 설정
                        applicationProgramRepository.save(applicationProgram);
                    }
                    case SOFTWARE -> {
                        Software software = new Software();
                        software.setAssetNo(commonAsset1);
                        software.setIp("192.168.1." + i);  // 더미 데이터 반영
                        software.setServerId("ServerID_" + i);  // 더미 데이터 반영
                        software.setServerPassword("password_" + i);  // 더미 데이터 반영
                        software.setCompanyManager("Manager " + i);  // 더미 데이터 반영
                        software.setOs("Windows Server 2022");  // 더미 데이터 반영
                        softwareRepository.save(software);
                    }
                    case ELECTRONIC_INFORMATION -> {
                        ElectronicInformation electronicInformation = new ElectronicInformation();
                        electronicInformation.setAssetNo(commonAsset1);
                        electronicInformation.setOs("Linux");  // 더미 데이터 반영
                        electronicInformation.setSystem("System_" + i);  // 더미 데이터 반영
                        electronicInformation.setDbtype("Database Type " + i);  // 더미 데이터 반영
                        electronicInformationRepository.save(electronicInformation);
                    }
                    case DOCUMENT -> {
                        Document document = new Document();
                        document.setAssetNo(commonAsset1);  // 자산 번호 설정
                        // DocumentType과 DocumentGrade를 순환적으로 설정
                        DocumentType documentType = DocumentType.values()[i % DocumentType.values().length]; // 0~3 인덱스로 순환
                        DocumentGrade documentGrade = DocumentGrade.values()[i % DocumentGrade.values().length]; // 0~2 인덱스로 순환
                        document.setDocumentType(documentType);  // DocumentType 설정
                        document.setDocumentGrade(documentGrade);  // DocumentGrade 설정
                        document.setDocumentLink("http://documentlink" + i + ".com");  // 더미 데이터 반영
                        documentRepository.save(document);
                    }
                    case PATENTS_AND_TRADEMARKS -> {
                        PatentsAndTrademarks patentsAndTrademarks = new PatentsAndTrademarks();
                        patentsAndTrademarks.setAssetNo(commonAsset1);  // 자산 번호 설정
                        patentsAndTrademarks.setApplicationDate(LocalDate.of(2020, i % 12 + 1, i % 28 + 1));  // 더미 데이터 반영
                        patentsAndTrademarks.setRegistrationDate(LocalDate.of(2021, i % 12 + 1, i % 28 + 1));  // 더미 데이터 반영
                        patentsAndTrademarks.setExpirationDate(LocalDate.of(2030, i % 12 + 1, i % 28 + 1));  // 더미 데이터 반영

                        // PatentTrademarkStatus, CountryApplication, PatentClassification, PatentItem을 순환적으로 설정
                        PatentTrademarkStatus status = PatentTrademarkStatus.values()[i % PatentTrademarkStatus.values().length]; // 0~3 인덱스로 순환
                        CountryApplication country = CountryApplication.values()[i % CountryApplication.values().length]; // 0~4 인덱스로 순환
                        PatentClassification patentClassification = PatentClassification.values()[i % PatentClassification.values().length]; // 0~1 인덱스로 순환
                        PatentItem item = PatentItem.values()[i % PatentItem.values().length]; // 0~1 인덱스로 순환

                        // 데이터 설정
                        patentsAndTrademarks.setPatentTrademarkStatus(status);  // 상태 설정
                        patentsAndTrademarks.setCountryApplication(country);  // 출원 국가 설정
                        patentsAndTrademarks.setPatentClassification(patentClassification);  // 특허 분류 설정
                        patentsAndTrademarks.setPatentItem(item);  // 특허 세목 설정
                        patentsAndTrademarks.setApplicationNo("AppNo_" + i);  // 더미 데이터 반영
                        patentsAndTrademarks.setInventor("Inventor " + i);  // 더미 데이터 반영
                        patentsAndTrademarks.setAssignee("Assignee " + i);  // 더미 데이터 반영

                        patentsAndTrademarksRepository.save(patentsAndTrademarks);
                    }
                    case ITSYSTEM_EQUIPMENT -> {
                        ItSystemEquipment itSystemEquipment = new ItSystemEquipment();
                        itSystemEquipment.setAssetNo(commonAsset1);  // 자산 번호 설정
                        itSystemEquipment.setEquipmentType("Type " + i);  // 더미 데이터 반영
                        // int를 Long으로 변환하여 설정

                        itSystemEquipment.setPowerSupply("Power Supply " + i);  // 더미 데이터 반영
                        itSystemEquipment.setCoolingSystem("Cooling System " + i);  // 더미 데이터 반영
                        itSystemEquipment.setInterfacePorts("Port " + i);  // 더미 데이터 반영
                        itSystemEquipment.setFormFactor("Form Factor " + i);  // 더미 데이터 반영
                        itSystemEquipment.setExpansionSlots(Long.valueOf(i));  // 더미 데이터 반영
                        itSystemEquipment.setGraphicsCard("Graphics Card " + i);  // 더미 데이터 반영
                        itSystemEquipment.setPortConfiguration("Port Configuration " + i);  // 더미 데이터 반영
                        itSystemEquipment.setMonitorIncluded(i % 2 == 0);  // 더미 데이터 반영
                        itSystemEquipmentRepository.save(itSystemEquipment);
                    }
                    case ITNETWORK_EQUIPMENT -> {
                        ItNetworkEquipment itNetworkEquipment = new ItNetworkEquipment();
                        itNetworkEquipment.setAssetNo(commonAsset1);  // 자산 번호 설정
                        itNetworkEquipment.setEquipmentType("Network Equipment Type " + i);  // 더미 데이터 반영
                        itNetworkEquipment.setRackUnit("Test Rack Unit");
                        itNetworkEquipment.setNumberOfPorts(Long.valueOf(i));  // 더미 데이터 반영
                        itNetworkEquipment.setSupportedProtocols("Protocol " + i);  // 더미 데이터 반영
                        itNetworkEquipment.setFirmwareVersion("v1." + i);  // 더미 데이터 반영
                        itNetworkEquipment.setNetworkSpeed(Long.valueOf(i));  // 더미 데이터 반영
                        itNetworkEquipment.setServiceScope("Service Scope " + i);  // 더미 데이터 반영
                        itNetworkEquipmentRepository.save(itNetworkEquipment);
                    }
                    case TERMINAL -> {
                        Terminal terminal = new Terminal();
                        terminal.setAssetNo(commonAsset1);
                        terminal.setIp("192.168.1." + i);  // 더미 데이터 반영
                        terminal.setOs("Terminal OS " + i);  // 더미 데이터 반영
                        SecurityControl securityControl = SecurityControl.values()[i % SecurityControl.values().length ];
                        terminal.setSecurityControl(securityControl);
                        // LocalDate 타입 필드 설정
                        terminal.setKaitsKeeper(LocalDate.now().plusDays(i));  // 현재 날짜에서 i일 더하기
                        terminal.setV3OfficeSecurity(LocalDate.now().plusDays(i + 1));  // 현재 날짜에서 i+1일 더하기
                        terminal.setAppCheckPro(LocalDate.now().plusDays(i + 2));  // 현재 날짜에서 i+2일 더하기
                        terminal.setTgate(LocalDate.now().plusDays(i + 3));  // 현재 날짜에서 i+3일 더하기
                        terminalRepository.save(terminal);
                    }
                    case FURNITURE -> {
                        Furniture furniture = new Furniture();
                        furniture.setAssetNo(commonAsset1);  // 자산 번호 설정
                        furniture.setFurnitureSize("Size " + i);  // 더미 데이터 반영
                        furnitureRepository.save(furniture);
                    }
                    case DEVICES -> {
                        Devices devices = new Devices();
                        devices.setAssetNo(commonAsset1);
                        devices.setDeviceType("Device Type " + i);  // 더미 데이터 반영
                        devices.setModelNumber("Model " + i);  // 더미 데이터 반영
                        devices.setConnectionType("Wired");  // 더미 데이터 반영
                        devices.setPowerSpecifications("5V, 2A");  // 더미 데이터 반영
                        devicesRepository.save(devices);
                    }
                    case CAR -> {
                        Car car = new Car();
                        car.setAssetNo(commonAsset1);  // 자산 번호 설정
                        car.setDisplacement(Long.valueOf(i));
                        car.setDoorsCount(Long.valueOf(i));
                        EngineType engineType = EngineType.values()[i % EngineType.values().length];
                        CarType carType = CarType.values()[i % CarType.values().length];
                        car.setEngineType(engineType);
                        car.setCarType(carType);
                        car.setIdentificationNo("ID" + i);
                        car.setCarColor("Black");
                        car.setModelYear(Long.valueOf(i));
                        carRepository.save(car);
                    }
                    case OTHERASSETS -> {
                        OtherAssets otherAssets = new OtherAssets();
                        otherAssets.setAssetNo(commonAsset1);
                        otherAssets.setOtherDescription("otherDescription " + i);
                        otherAssets.setUsageFrequency("usageFrequency" + i);
                        otherAssetsRepository.save(otherAssets);
                    }
                }

                // 15개씩 생성했으면 다음 분류로 이동
                if (i % assetCountPerClassification == 0) {
                    classificationIndex++;
                }
            }
    }
}
