package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.AssetDisposeDto;
import com.codehows.taelim.dto.AssetUpdateDto;
import com.codehows.taelim.dto.ExcelDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RegisterService {

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
    private final MemberRepository memberRepository;
    private final DemandRepository demandRepository;
    private final DemandDtlRepository demandDtlRepository;



    //자산 등록
    public Long assetRegister(AssetDto assetDto){

//        Member assetUser = memberRepository.findByEmail(assetDto.getAssetUser());
//        Member assetOwner = memberRepository.findByEmail(assetDto.getAssetOwner());
//        Member assetSecurityManager = memberRepository.findByEmail(assetDto.getAssetSecurityManager());
        CommonAsset commonAsset = assetDto.toEntity();
//        commonAsset.setAssetUser(assetUser);
//        commonAsset.setAssetOwner(assetOwner);
//        commonAsset.setAssetSecurityManager(assetSecurityManager);
        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.FALSE);
        commonAsset.setDemandStatus(Boolean.FALSE);
        commonAsset.setDemandCheck(Boolean.FALSE);
        commonAsset.setCreateDate(LocalDate.now());

        // 자산코드 생성
        String assetCode = generateAssetCode(commonAsset.getAssetClassification());
        commonAsset.setAssetCode(assetCode);

        commonAssetRepository.save(commonAsset);

        Long assetId = commonAsset.getAssetNo();
        CommonAsset commonAsset1 = commonAssetRepository.findTopByOrderByAssetNoDesc();

        switch (commonAsset.getAssetClassification()){
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem informationProtectionSystem = assetDto.toInformationProtectionSystem();
                informationProtectionSystem.setAssetNo(commonAsset1);
                informationProtectionSystemRepository.save(informationProtectionSystem);
            }
            case APPLICATION_PROGRAM -> {
                ApplicationProgram applicationProgram = assetDto.toApplication();
                applicationProgram.setAssetNo(commonAsset1);
                applicationProgramRepository.save(applicationProgram);
            }
            case SOFTWARE -> {
                Software software = assetDto.toSoftware();
                software.setAssetNo(commonAsset1);
                softwareRepository.save(software);
            }
            case ELECTRONIC_INFORMATION -> {
                ElectronicInformation electronicInformation = assetDto.toElectronicInformation();
                electronicInformation.setAssetNo(commonAsset1);
                electronicInformationRepository.save(electronicInformation);
            }
            case DOCUMENT -> {
                Document document = assetDto.toDocumnet();
                document.setAssetNo(commonAsset1);
                documentRepository.save(document);
            }
            case PATENTS_AND_TRADEMARKS -> {
                PatentsAndTrademarks patentsAndTrademarks = assetDto.toPatentsAndTrademarks();
                patentsAndTrademarks.setAssetNo(commonAsset1);
                patentsAndTrademarksRepository.save(patentsAndTrademarks);
            }
            case ITSYSTEM_EQUIPMENT -> {
                ItSystemEquipment itSystemEquipment = assetDto.toItSystemEquipment();
                itSystemEquipment.setAssetNo(commonAsset1);
                itSystemEquipmentRepository.save(itSystemEquipment);
            }
            case ITNETWORK_EQUIPMENT -> {
                ItNetworkEquipment itNetworkEquipment = assetDto.toItNetworkEquipment();
                itNetworkEquipment.setAssetNo(commonAsset1);
                itNetworkEquipmentRepository.save(itNetworkEquipment);
            }
            case TERMINAL -> {
                Terminal terminal = assetDto.toTerminal();
                terminal.setAssetNo(commonAsset1);
                terminalRepository.save(terminal);
            }
            case FURNITURE -> {
                Furniture furniture = assetDto.toFurniture();
                furniture.setAssetNo(commonAsset1);
                furnitureRepository.save(furniture);
            }
            case DEVICES -> {
                Devices devices = assetDto.toDevices();
                devices.setAssetNo(commonAsset1);
                devicesRepository.save(devices);
            }
            case CAR -> {
                Car car = assetDto.toCar();
                car.setAssetNo(commonAsset1);
                carRepository.save(car);
            }
            case OTHERASSETS -> {
                OtherAssets otherAssets = assetDto.toOtherAssets();
                otherAssets.setAssetNo(commonAsset1);
                otherAssetsRepository.save(otherAssets);
            }
        }
        return assetId;

    }

    @Transactional
    //엑셀로 등록
    public void excelRegister (ExcelDto excelDto) {

//        Member assetOwner = memberRepository.findByUName(excelDto.getAssetOwner())
//                .orElse(new Member(excelDto.getAssetOwner()));
//        Member assetUser = memberRepository.findByUName(excelDto.getAssetUser())
//                .orElse(new Member(excelDto.getAssetUser()));
//        Member assetSecurityManager = memberRepository.findByUName(excelDto.getAssetSecurityManager())
//                .orElse(new Member(excelDto.getAssetSecurityManager()));
        CommonAsset commonAsset = excelDto.toExcel();
//        commonAsset.setAssetOwner(assetOwner);
//        commonAsset.setAssetUser(assetUser);
//
//        commonAsset.setAssetSecurityManager(assetSecurityManager);
        commonAssetRepository.save(commonAsset);

        CommonAsset commonAsset1 = commonAssetRepository.findTopByOrderByAssetNoDesc();

        InformationProtectionSystem informationProtectionSystem = excelDto.toExcelInfo();
        informationProtectionSystem.setAssetNo(commonAsset1);

        informationProtectionSystemRepository.save(informationProtectionSystem);

    }
    @Transactional
    public void excelRegisterAll(List<ExcelDto> excelDtos) {
        for (ExcelDto excelDto : excelDtos) {
            excelRegister(excelDto);
        }
    }
    public Optional<CommonAsset> findById(Long id) {
        return commonAssetRepository.findById(id);
    }
    public List<AssetDto> findAll() {
        List<CommonAsset> assets = commonAssetRepository.findAll();
        return assets.stream()
                .map(asset -> {
                    AssetDto dto = new AssetDto();
                    dto.setAssetNo(asset.getAssetNo());
                    dto.setAssetClassification(asset.getAssetClassification());
                    dto.setAssetBasis(asset.getAssetBasis());
                    dto.setAssetCode(asset.getAssetCode());
                    dto.setAssetName(asset.getAssetName());
                    dto.setPurpose(asset.getPurpose());
                    dto.setDepartment(asset.getDepartment());
                    dto.setAssetLocation(asset.getAssetLocation());
                    dto.setOperationStatus(asset.getOperationStatus());
                    dto.setPurchaseDate(asset.getPurchaseDate());
                    dto.setManufacturingCompany(asset.getManufacturingCompany());
                    dto.setWarrantyDetails(asset.getWarrantyDetails());
                    dto.setApproval(asset.getApproval());
                    dto.setAttachment(asset.getAttachment());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 자산 코드 생성
    private String generateAssetCode(AssetClassification classification) {
        String prefix = "";
        String classificationCode = "";

        switch (classification){
            case INFORMATION_PROTECTION_SYSTEM -> classificationCode = "IPS";
            case APPLICATION_PROGRAM -> classificationCode = "APP";
            case SOFTWARE -> classificationCode = "SWR";
            case ELECTRONIC_INFORMATION -> classificationCode = "EIN";
            case DOCUMENT -> classificationCode = "DOC";
            case PATENTS_AND_TRADEMARKS -> classificationCode = "PAT";
            case ITSYSTEM_EQUIPMENT -> classificationCode = "ITS";
            case ITNETWORK_EQUIPMENT -> classificationCode = "ITN" ;
            case TERMINAL -> classificationCode = "TER";
            case FURNITURE -> classificationCode = "FUR" ;
            case DEVICES -> classificationCode = "DEV" ;
            case CAR -> classificationCode = "CAR";
            case OTHERASSETS -> classificationCode = "OTA";
        }

        // 앞에 접두사로 3자리 코드
        if (classificationCode.startsWith("I") || classificationCode.startsWith("A") ||
            classificationCode.startsWith("S") || classificationCode.startsWith("E") ||
            classificationCode.startsWith("DO") || classificationCode.startsWith("P")) {
            prefix = "TLIA-";
        } else {
            prefix = "ASST-";
        }

        // 최신 자산코드를 조회하여 숫자 증가
        Optional<String> lastAssetCodeOpt = commonAssetRepository.findLastAssetCodeByClassification(classification);
        int newAssetNumber = 1;

        // Optional 값을 처리하여 lastAssetCode로 변환
        if (lastAssetCodeOpt.isPresent() && lastAssetCodeOpt.get().matches(".*\\d+$")) {
            String lastAssetCode = lastAssetCodeOpt.get();  // Optional에서 값 추출
            String numberPart = lastAssetCode.replaceAll("\\D+", "");  // 숫자만 추출
            newAssetNumber = Integer.parseInt(numberPart) + 1;
        }

        // 자산 코드 포맷 : 예) TLIA-IPS-00001
        return String.format("%s%s-%05d", prefix, classificationCode, newAssetNumber);
    }

    public Long updateAssetCode(String assetCode, AssetUpdateDto assetDto) {

        // 기존 입력되어있는 assetCode 조회
        CommonAsset existAsset = commonAssetRepository.findLatestAssetCode(assetCode)
                .orElseThrow(() -> new RuntimeException("자산코드를 찾을수 없음 " + assetCode));

        // 기존 자산 정보에 새로운 자산 생성
        CommonAsset updateAsset = new CommonAsset();
        updateAsset.setAssetCode(existAsset.getAssetCode()); // 코드 동일하게 유지하고
        updateAsset.setAssetName(existAsset.getAssetName());
        updateAsset.setAssetBasis(existAsset.getAssetBasis());
        updateAsset.setManufacturingCompany(existAsset.getManufacturingCompany());
        updateAsset.setPurpose(existAsset.getPurpose());
//        updateAsset.setAssetUser(existAsset.getAssetUser());    // 사용자들은 나중에 바꿔야함
//        updateAsset.setAssetOwner(existAsset.getAssetOwner());
//        updateAsset.setAssetSecurityManager(existAsset.getAssetSecurityManager());

        // AssetDto에서 업데이트할 필드 설정 (null 체크 후 기존 값 유지)
        updateAsset.setDepartment(assetDto.getDepartment() != null ? assetDto.getDepartment() : existAsset.getDepartment());
        updateAsset.setAssetLocation(assetDto.getAssetLocation() != null ? assetDto.getAssetLocation() : existAsset.getAssetLocation());
        //updateAsset.setAssetUser(assetDto.getAssetUser() != null ? assetDto.getAssetUser() : existAsset.getAssetUser());
        //updateAsset.setAssetOwner(assetDto.getAssetOwner() != null ? assetDto.getAssetOwner() : existAsset.getAssetOwner());
        //updateAsset.setAssetSecurityManager(assetDto.getAssetSecurityManager() != null ? assetDto.getAssetSecurityManager() : existAsset.getAssetSecurityManager());
        updateAsset.setUseState(assetDto.getUseState() != null ? assetDto.getUseState() : existAsset.getUseState());
        updateAsset.setOperationStatus(assetDto.getOperationStatus() != null ? assetDto.getOperationStatus() : existAsset.getOperationStatus());
        updateAsset.setIntroducedDate(assetDto.getIntroducedDate() != null ? assetDto.getIntroducedDate() : existAsset.getIntroducedDate());
        // int 필드에 대해 기본값 처리
        updateAsset.setConfidentiality(assetDto.getConfidentiality() != 0 ? assetDto.getConfidentiality() : existAsset.getConfidentiality());
        updateAsset.setIntegrity(assetDto.getIntegrity() != 0 ? assetDto.getIntegrity() : existAsset.getIntegrity());
        updateAsset.setAvailability(assetDto.getAvailability() != 0 ? assetDto.getAvailability() : existAsset.getAvailability());
        // 다시
        updateAsset.setNote(assetDto.getNote() != null ? assetDto.getNote() : existAsset.getNote());
        updateAsset.setPurchaseCost(assetDto.getPurchaseCost() != null ? assetDto.getPurchaseCost() : existAsset.getPurchaseCost());
        updateAsset.setPurchaseDate(assetDto.getPurchaseDate() != null ? assetDto.getPurchaseDate() : existAsset.getPurchaseDate());
        updateAsset.setUsefulLife(assetDto.getUsefulLife() != null ? assetDto.getUsefulLife() : existAsset.getUsefulLife());
        updateAsset.setDepreciationMethod(assetDto.getDepreciationMethod() != null ? assetDto.getDepreciationMethod() : existAsset.getDepreciationMethod());
        updateAsset.setPurchaseSource(assetDto.getPurchaseSource() != null ? assetDto.getPurchaseSource() : existAsset.getPurchaseSource());
        updateAsset.setContactInformation(assetDto.getContactInformation() != null ? assetDto.getContactInformation() : existAsset.getContactInformation());
        updateAsset.setAcquisitionRoute(assetDto.getAcquisitionRoute() != null ? assetDto.getAcquisitionRoute() : existAsset.getAcquisitionRoute());
        updateAsset.setMaintenancePeriod(assetDto.getMaintenancePeriod() != null ? assetDto.getMaintenancePeriod() : existAsset.getMaintenancePeriod());

        updateAsset.setAssetClassification(assetDto.getAssetClassification() != null ? assetDto.getAssetClassification() : existAsset.getAssetClassification() // 기본값 설정
        );
        //자산정보에 따른 세부 변경사항
        updateAsset.setApproval(Approval.APPROVE);
        updateAsset.setDisposalStatus(Boolean.FALSE);
        updateAsset.setDemandStatus(Boolean.FALSE);
        updateAsset.setDemandCheck(Boolean.FALSE);
        updateAsset.setCreateDate(LocalDate.now());  // 등록일 갱신

        commonAssetRepository.save(updateAsset);

        // 새로운 자산 번호 생성
        Long newAssetNo = updateAsset.getAssetNo();
        CommonAsset latestAsset = commonAssetRepository.findTopByOrderByAssetNoDesc();
        // 자산 분류에 따라 관련된 데이터베이스 저장
        saveRelatedEntity(assetDto, latestAsset);

        // 수정 이력 저장
        Demand demand = new Demand();
        //demand.setDemandBy(); 추후 사람
        demand.setDemandDate(assetDto.getCreateDate());
        demand.setDemandReason(assetDto.getUpdateReason());
        demand.setDemandDetail(assetDto.getUpdateDetail());
        demandRepository.save(demand);
        // DemandDtl 테이블 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(updateAsset);
        demandDtl.setDemandNo(demand);
        demandDtlRepository.save(demandDtl);

        return newAssetNo;
    }

    public Long updatedemandAssetCode(String assetCode, AssetUpdateDto assetDto) {

        // 기존 입력되어있는 assetCode 조회
        CommonAsset existAsset = commonAssetRepository.findLatestAssetCode(assetCode)
                .orElseThrow(() -> new RuntimeException("자산코드를 찾을수 없음 " + assetCode));

        // 기존 자산 정보에 새로운 자산 생성
        CommonAsset updateAsset = new CommonAsset();
        updateAsset.setAssetCode(existAsset.getAssetCode()); // 코드 동일하게 유지하고
        updateAsset.setAssetName(existAsset.getAssetName());
        updateAsset.setAssetBasis(existAsset.getAssetBasis());
        updateAsset.setManufacturingCompany(existAsset.getManufacturingCompany());
        updateAsset.setPurpose(existAsset.getPurpose());
//        updateAsset.setAssetUser(existAsset.getAssetUser());    // 사용자들은 나중에 바꿔야함
//        updateAsset.setAssetOwner(existAsset.getAssetOwner());
//        updateAsset.setAssetSecurityManager(existAsset.getAssetSecurityManager());

        // AssetDto에서 업데이트할 필드 설정 (null 체크 후 기존 값 유지)
        updateAsset.setDepartment(assetDto.getDepartment() != null ? assetDto.getDepartment() : existAsset.getDepartment());
        updateAsset.setAssetLocation(assetDto.getAssetLocation() != null ? assetDto.getAssetLocation() : existAsset.getAssetLocation());
        //updateAsset.setAssetUser(assetDto.getAssetUser() != null ? assetDto.getAssetUser() : existAsset.getAssetUser());
        //updateAsset.setAssetOwner(assetDto.getAssetOwner() != null ? assetDto.getAssetOwner() : existAsset.getAssetOwner());
        //updateAsset.setAssetSecurityManager(assetDto.getAssetSecurityManager() != null ? assetDto.getAssetSecurityManager() : existAsset.getAssetSecurityManager());
        updateAsset.setUseState(assetDto.getUseState() != null ? assetDto.getUseState() : existAsset.getUseState());
        updateAsset.setOperationStatus(assetDto.getOperationStatus() != null ? assetDto.getOperationStatus() : existAsset.getOperationStatus());
        updateAsset.setIntroducedDate(assetDto.getIntroducedDate() != null ? assetDto.getIntroducedDate() : existAsset.getIntroducedDate());
        // int 필드에 대해 기본값 처리
        updateAsset.setConfidentiality(assetDto.getConfidentiality() != 0 ? assetDto.getConfidentiality() : existAsset.getConfidentiality());
        updateAsset.setIntegrity(assetDto.getIntegrity() != 0 ? assetDto.getIntegrity() : existAsset.getIntegrity());
        updateAsset.setAvailability(assetDto.getAvailability() != 0 ? assetDto.getAvailability() : existAsset.getAvailability());
        // 다시
        updateAsset.setNote(assetDto.getNote() != null ? assetDto.getNote() : existAsset.getNote());
        updateAsset.setPurchaseCost(assetDto.getPurchaseCost() != null ? assetDto.getPurchaseCost() : existAsset.getPurchaseCost());
        updateAsset.setPurchaseDate(assetDto.getPurchaseDate() != null ? assetDto.getPurchaseDate() : existAsset.getPurchaseDate());
        updateAsset.setUsefulLife(assetDto.getUsefulLife() != null ? assetDto.getUsefulLife() : existAsset.getUsefulLife());
        updateAsset.setDepreciationMethod(assetDto.getDepreciationMethod() != null ? assetDto.getDepreciationMethod() : existAsset.getDepreciationMethod());
        updateAsset.setPurchaseSource(assetDto.getPurchaseSource() != null ? assetDto.getPurchaseSource() : existAsset.getPurchaseSource());
        updateAsset.setContactInformation(assetDto.getContactInformation() != null ? assetDto.getContactInformation() : existAsset.getContactInformation());
        updateAsset.setAcquisitionRoute(assetDto.getAcquisitionRoute() != null ? assetDto.getAcquisitionRoute() : existAsset.getAcquisitionRoute());
        updateAsset.setMaintenancePeriod(assetDto.getMaintenancePeriod() != null ? assetDto.getMaintenancePeriod() : existAsset.getMaintenancePeriod());

        updateAsset.setAssetClassification(assetDto.getAssetClassification() != null ? assetDto.getAssetClassification() : existAsset.getAssetClassification() // 기본값 설정
        );
        //자산정보에 따른 세부 변경사항
        updateAsset.setApproval(Approval.UNCONFIRMED);
        updateAsset.setDisposalStatus(Boolean.FALSE);
        updateAsset.setDemandStatus(Boolean.TRUE);
        updateAsset.setDemandCheck(Boolean.TRUE);
        updateAsset.setCreateDate(LocalDate.now());  // 등록일 갱신

        commonAssetRepository.save(updateAsset);

        // 새로운 자산 번호 생성
        Long newAssetNo = updateAsset.getAssetNo();
        CommonAsset latestAsset = commonAssetRepository.findTopByOrderByAssetNoDesc();
        // 자산 분류에 따라 관련된 데이터베이스 저장
        saveRelatedEntity(assetDto, latestAsset);

        // 수정 이력 저장
        Demand demand = new Demand();
        //demand.setDemandBy(); 추후 사람
        demand.setDemandDate(assetDto.getCreateDate());
        demand.setDemandReason(assetDto.getUpdateReason());
        demand.setDemandDetail(assetDto.getUpdateDetail());
        demandRepository.save(demand);
        // DemandDtl 테이블 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(updateAsset);
        demandDtl.setDemandNo(demand);
        demandDtlRepository.save(demandDtl);

        return newAssetNo;
    }
    private void saveRelatedEntity(AssetUpdateDto assetDto, CommonAsset latestAsset) {
        if (latestAsset.getAssetClassification() == null) {
            throw new IllegalArgumentException("Asset classification cannot be null.");
        }

        switch (latestAsset.getAssetClassification()) {
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem infoSystem = assetDto.toInformationProtectionSystem();
                infoSystem.setAssetNo(latestAsset);
                informationProtectionSystemRepository.save(infoSystem);
            }
            case APPLICATION_PROGRAM -> {
                ApplicationProgram appProgram = assetDto.toApplication();
                appProgram.setAssetNo(latestAsset);
                applicationProgramRepository.save(appProgram);
            }
            case SOFTWARE -> {
                Software software = assetDto.toSoftware();
                software.setAssetNo(latestAsset);
                softwareRepository.save(software);
            }
            case ELECTRONIC_INFORMATION -> {
                ElectronicInformation elecInfo = assetDto.toElectronicInformation();
                elecInfo.setAssetNo(latestAsset);
                electronicInformationRepository.save(elecInfo);
            }
            case DOCUMENT -> {
                Document document = assetDto.toDocumnet();
                document.setAssetNo(latestAsset);
                documentRepository.save(document);
            }
            case PATENTS_AND_TRADEMARKS -> {
                PatentsAndTrademarks patents = assetDto.toPatentsAndTrademarks();
                patents.setAssetNo(latestAsset);
                patentsAndTrademarksRepository.save(patents);
            }
            case ITSYSTEM_EQUIPMENT -> {
                ItSystemEquipment itEquipment = assetDto.toItSystemEquipment();
                itEquipment.setAssetNo(latestAsset);
                itSystemEquipmentRepository.save(itEquipment);
            }
            case ITNETWORK_EQUIPMENT -> {
                ItNetworkEquipment networkEquipment = assetDto.toItNetworkEquipment();
                networkEquipment.setAssetNo(latestAsset);
                itNetworkEquipmentRepository.save(networkEquipment);
            }
            case TERMINAL -> {
                Terminal terminal = assetDto.toTerminal();
                terminal.setAssetNo(latestAsset);
                terminalRepository.save(terminal);
            }
            case FURNITURE -> {
                Furniture furniture = assetDto.toFurniture();
                furniture.setAssetNo(latestAsset);
                furnitureRepository.save(furniture);
            }
            case DEVICES -> {
                Devices devices = assetDto.toDevices();
                devices.setAssetNo(latestAsset);
                devicesRepository.save(devices);
            }
            case CAR -> {
                Car car = assetDto.toCar();
                car.setAssetNo(latestAsset);
                carRepository.save(car);
            }
            case OTHERASSETS -> {
                OtherAssets otherAssets = assetDto.toOtherAssets();
                otherAssets.setAssetNo(latestAsset);
                otherAssetsRepository.save(otherAssets);
            }
            default -> throw new IllegalArgumentException("Unknown asset classification: " + latestAsset.getAssetClassification());
        }
    }

    // 폐기 담당자 요청처리 에  새로운 자산을 만들어서 처리하는 개념
    public Long DisposeDemand(String assetCode, AssetDisposeDto assetDisposeDto) {

        // 1. 기존 입력되어있는 assetCode 조회
        CommonAsset existAsset = commonAssetRepository.findLatestAssetCode(assetCode)
                .orElseThrow(() -> new RuntimeException("자산코드를 찾을수 없음 " + assetCode));

        // 2. 새로운 자산 생성
        CommonAsset demandAsset = new CommonAsset();
        demandAsset.setAssetCode(existAsset.getAssetCode()); // 코드 동일하게 유지하고
        demandAsset.setAssetName(existAsset.getAssetName());
        demandAsset.setAssetBasis(existAsset.getAssetBasis());
        demandAsset.setManufacturingCompany(existAsset.getManufacturingCompany());
        demandAsset.setPurpose(existAsset.getPurpose());
//        updateAsset.setAssetUser(existAsset.getAssetUser());    // 사용자들은 나중에 바꿔야함
//        updateAsset.setAssetOwner(existAsset.getAssetOwner());
//        updateAsset.setAssetSecurityManager(existAsset.getAssetSecurityManager());

        // AssetDto에서 업데이트할 필드 설정 (null 체크 후 기존 값 유지)
        demandAsset.setDepartment(existAsset.getDepartment());
        demandAsset.setAssetLocation(existAsset.getAssetLocation());
        //updateAsset.setAssetUser(existAsset.getAssetUser());
        //updateAsset.setAssetOwner(existAsset.getAssetOwner());
        //updateAsset.setAssetSecurityManager(existAsset.getAssetSecurityManager());
        demandAsset.setUseState(existAsset.getUseState());
        demandAsset.setOperationStatus(existAsset.getOperationStatus());
        demandAsset.setIntroducedDate(existAsset.getIntroducedDate());
        // int 필드에 대해 기본값 처리
        demandAsset.setConfidentiality(existAsset.getConfidentiality());
        demandAsset.setIntegrity(existAsset.getIntegrity());
        demandAsset.setAvailability(existAsset.getAvailability());
        // 다시
        demandAsset.setNote(existAsset.getNote());
        demandAsset.setPurchaseCost(existAsset.getPurchaseCost());
        demandAsset.setPurchaseDate(existAsset.getPurchaseDate());
        demandAsset.setUsefulLife(existAsset.getUsefulLife());
        demandAsset.setDepreciationMethod(existAsset.getDepreciationMethod());
        demandAsset.setPurchaseSource(existAsset.getPurchaseSource());
        demandAsset.setContactInformation(existAsset.getContactInformation());
        demandAsset.setAcquisitionRoute(existAsset.getAcquisitionRoute());
        demandAsset.setMaintenancePeriod(existAsset.getMaintenancePeriod());
        demandAsset.setAssetClassification(existAsset.getAssetClassification() // 기본값 설정
        );


        demandAsset.setApproval(Approval.UNCONFIRMED);
        demandAsset.setDisposalStatus(Boolean.TRUE);
        demandAsset.setDemandStatus(Boolean.TRUE);
        demandAsset.setDemandCheck(Boolean.TRUE);
        demandAsset.setCreateDate(LocalDate.now());

        commonAssetRepository.save(demandAsset);

        // 폐기 요청도 결국 새로운 자산 번호 생성해야함
        Long newAssetNo = demandAsset.getAssetNo();

        CommonAsset latestAsset = commonAssetRepository.findTopByOrderByAssetNoDesc();
        //자산 분류에 따라 관련된 데이터 베이스 저장
        saveRelatedEntity1(assetDisposeDto, latestAsset);

        // 폐기 이력 저장
        Demand demand = new Demand();
        //demand.setDemandBy;
        demand.setDemandDate(LocalDate.now()); // 폐기 일자 - 추후 자동생성 변경
        demand.setDemandReason(assetDisposeDto.getDisposeReason()); // 폐기 사유
        demand.setDemandDetail(assetDisposeDto.getDisposeDetail()); // 폐기내용
        demand.setDisposeMethod(assetDisposeDto.getDisposeMethod()); // 폐기 방법
        demand.setDisposeLocation(assetDisposeDto.getDisposeLocation());  // 폐기 위치
        demandRepository.save(demand);

        // DemandDtl 테이블에 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(demandAsset);  // CommonAsset과 연관 설정
        demandDtl.setDemandNo(demand);  // Demand와 연관 설정
        demandDtlRepository.save(demandDtl); // DemandDtl 테이블에 저장

//        // 이메일 전송 기능 추가
//        try {
//            String toEmail = "kydea2240@example.com"; // 폐기 요청 담당자 이메일
//            String subject = "폐기 요청 알림";
//            String body = "자산코드: " + assetCode + "의 폐기 요청이 접수되었습니다.";
//
//            emailService.sendEmail(toEmail, subject, body);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            // 이메일 전송 실패 시 로그 기록 또는 예외 처리
//        }

        return newAssetNo;
    }

    private void saveRelatedEntity1(AssetDisposeDto assetDto, CommonAsset latestAsset) {
        if (latestAsset.getAssetClassification() == null) {
            throw new IllegalArgumentException("Asset classification cannot be null.");
        }

        switch (latestAsset.getAssetClassification()) {
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem infoSystem = assetDto.toInformationProtectionSystem();
                infoSystem.setAssetNo(latestAsset);
                informationProtectionSystemRepository.save(infoSystem);
            }
            case APPLICATION_PROGRAM -> {
                ApplicationProgram appProgram = assetDto.toApplication();
                appProgram.setAssetNo(latestAsset);
                applicationProgramRepository.save(appProgram);
            }
            case SOFTWARE -> {
                Software software = assetDto.toSoftware();
                software.setAssetNo(latestAsset);
                softwareRepository.save(software);
            }
            case ELECTRONIC_INFORMATION -> {
                ElectronicInformation elecInfo = assetDto.toElectronicInformation();
                elecInfo.setAssetNo(latestAsset);
                electronicInformationRepository.save(elecInfo);
            }
            case DOCUMENT -> {
                Document document = assetDto.toDocumnet();
                document.setAssetNo(latestAsset);
                documentRepository.save(document);
            }
            case PATENTS_AND_TRADEMARKS -> {
                PatentsAndTrademarks patents = assetDto.toPatentsAndTrademarks();
                patents.setAssetNo(latestAsset);
                patentsAndTrademarksRepository.save(patents);
            }
            case ITSYSTEM_EQUIPMENT -> {
                ItSystemEquipment itEquipment = assetDto.toItSystemEquipment();
                itEquipment.setAssetNo(latestAsset);
                itSystemEquipmentRepository.save(itEquipment);
            }
            case ITNETWORK_EQUIPMENT -> {
                ItNetworkEquipment networkEquipment = assetDto.toItNetworkEquipment();
                networkEquipment.setAssetNo(latestAsset);
                itNetworkEquipmentRepository.save(networkEquipment);
            }
            case TERMINAL -> {
                Terminal terminal = assetDto.toTerminal();
                terminal.setAssetNo(latestAsset);
                terminalRepository.save(terminal);
            }
            case FURNITURE -> {
                Furniture furniture = assetDto.toFurniture();
                furniture.setAssetNo(latestAsset);
                furnitureRepository.save(furniture);
            }
            case DEVICES -> {
                Devices devices = assetDto.toDevices();
                devices.setAssetNo(latestAsset);
                devicesRepository.save(devices);
            }
            case CAR -> {
                Car car = assetDto.toCar();
                car.setAssetNo(latestAsset);
                carRepository.save(car);
            }
            case OTHERASSETS -> {
                OtherAssets otherAssets = assetDto.toOtherAssets();
                otherAssets.setAssetNo(latestAsset);
                otherAssetsRepository.save(otherAssets);
            }
            default -> throw new IllegalArgumentException("Unknown asset classification: " + latestAsset.getAssetClassification());
        }
    }

    // 일괄 수정
    public Long allUpdate(AllUpdateDto allUpdateDto, Demand demand) {
        // Optional 처리 기존 자산 불러오기
        CommonAsset existAsset = commonAssetRepository.findById(allUpdateDto.getAssetNo())
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
        //기존 자산 Dto로 변환
        CommonAssetDto commonAssetDto = CommonAssetDto.fromEntity(existAsset);
        //assetNo를 null로
        commonAssetDto.setAssetNo(null);
        commonAssetDto.setApproval(Approval.APPROVE);
        commonAssetDto.setDisposalStatus(Boolean.FALSE);
        commonAssetDto.setDemandStatus(Boolean.FALSE);
        commonAssetDto.setDemandCheck(Boolean.FALSE);
        commonAssetDto.setCreateDate(LocalDate.now());  // 등록일 갱신
        //Dto를 엔티티로 변환
        CommonAsset commonAsset = commonAssetDto.toEntity(commonAssetDto);
        // 저장해서 새로운 자산 만들기
        CommonAsset updateAsset = commonAssetRepository.save(commonAsset);
        // 기존자산과 똑같은 서브컬럼 복사
        updateAssetBasedOnClassification(updateAsset, existAsset);

        // DemandDtl 테이블 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(updateAsset);
        demandDtl.setDemandNo(demand);
        demandDtlRepository.save(demandDtl);

        return updateAsset.getAssetNo();
    }
    public Demand UpdateDemand(AllUpdateDto allUpdateDto){
        Demand demand = new Demand();
        //demand.setDemandBy(); // 추후 사람
        demand.setDemandDate(LocalDate.now());
        demand.setDemandReason(allUpdateDto.getReason());
        demand.setDemandDetail(allUpdateDto.getDetail());

        return demandRepository.save(demand);
    }


    //일괄 수정 요청
    public Long allUpdateDemand(AllUpdateDto allUpdateDto, Demand demand) {
        // Optional 처리 기존 자산 불러오기
        CommonAsset existAsset = commonAssetRepository.findById(allUpdateDto.getAssetNo())
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));

        //기존 자산 Dto로 변환
        CommonAssetDto commonAssetDto = CommonAssetDto.fromEntity(existAsset);
        //assetNo를 null로
        commonAssetDto.setAssetNo(null);
        //자산정보에 따른 세부 변경사항
        commonAssetDto.setApproval(Approval.UNCONFIRMED);
        commonAssetDto.setDisposalStatus(Boolean.FALSE);
        commonAssetDto.setDemandStatus(Boolean.TRUE);
        commonAssetDto.setDemandCheck(Boolean.TRUE);
        commonAssetDto.setCreateDate(LocalDate.now());  // 등록일 갱신

        //Dto를 엔티티로 변환
        CommonAsset commonAsset = commonAssetDto.toEntity(commonAssetDto);
        // 저장해서 새로운 자산 만들기
        CommonAsset updateAsset = commonAssetRepository.save(commonAsset);
        // 기존자산과 똑같은 서브컬럼 복사
        updateAssetBasedOnClassification(updateAsset, existAsset);

        // DemandDtl 테이블 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(updateAsset);
        demandDtl.setDemandNo(demand);
        demandDtlRepository.save(demandDtl);

        return updateAsset.getAssetNo();
    }

    //일괄 폐기
    public Long allDelete(AllDeleteDto allDeleteDto, Demand demand) {
        // Optional 처리
        CommonAsset existAsset = commonAssetRepository.findById(allDeleteDto.getAssetNo())
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));

        CommonAssetDto commonAssetDto = CommonAssetDto.fromEntity(existAsset);
        commonAssetDto.setAssetNo(null);
        commonAssetDto.setApproval(Approval.APPROVE);
        commonAssetDto.setDisposalStatus(Boolean.TRUE);
        CommonAsset commonAsset = commonAssetDto.toEntity(commonAssetDto);
        CommonAsset updateAsset = commonAssetRepository.save(commonAsset);

        updateAssetBasedOnClassification(updateAsset, existAsset);

        // DemandDtl 테이블에 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(updateAsset);  // CommonAsset과 연관 설정
        demandDtl.setDemandNo(demand);  // Demand와 연관 설정
        demandDtlRepository.save(demandDtl); // DemandDtl 테이블에 저장

        return updateAsset.getAssetNo();

    }

    public Demand DeleteDemand(AllDeleteDto allDeleteDto){
        // 폐기 이력 저장
        Demand demand = new Demand();
        //demand.setDemandBy;
        demand.setDemandDate(LocalDate.now()); // 폐기 일자 - 추후 자동생성 변경
        demand.setDemandReason(allDeleteDto.getReason()); // 폐기 사유
        demand.setDemandDetail(allDeleteDto.getDetail()); // 폐기내용
        demand.setDisposeMethod(allDeleteDto.getDisposeMethod()); // 폐기 방법
        demand.setDisposeLocation(allDeleteDto.getDisposeLocation());  // 폐기 위치
        return demandRepository.save(demand);
    }

    //일괄 요청 폐기
    public Long allDeleteDemamd(AllDeleteDto allDeleteDto, Demand demand) {
        // Optional 처리
        CommonAsset existAsset = commonAssetRepository.findById(allDeleteDto.getAssetNo())
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));

        CommonAssetDto commonAssetDto = CommonAssetDto.fromEntity(existAsset);
        commonAssetDto.setAssetNo(null);
        // AssetDto에서 업데이트할 필드 설정 (null 체크 후 기존 값 유지)
        commonAssetDto.setApproval(Approval.UNCONFIRMED);
        commonAssetDto.setDisposalStatus(Boolean.TRUE);
        commonAssetDto.setDemandStatus(Boolean.TRUE);
        commonAssetDto.setDemandCheck(Boolean.TRUE);
        commonAssetDto.setCreateDate(LocalDate.now());

        //updateAsset.setAssetUser(existAsset.getAssetUser());
        //updateAsset.setAssetOwner(existAsset.getAssetOwner());

        CommonAsset commonAsset = commonAssetDto.toEntity(commonAssetDto);
        CommonAsset updateAsset = commonAssetRepository.save(commonAsset);

        updateAssetBasedOnClassification(updateAsset, existAsset);

        // DemandDtl 테이블에 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(updateAsset);  // CommonAsset과 연관 설정
        demandDtl.setDemandNo(demand);  // Demand와 연관 설정
        demandDtlRepository.save(demandDtl); // DemandDtl 테이블에 저장

        return updateAsset.getAssetNo();
    }

    private void updateAssetBasedOnClassification(CommonAsset updateAsset, CommonAsset existAsset) {
        switch (updateAsset.getAssetClassification()) {
            case INFORMATION_PROTECTION_SYSTEM -> updateInformationProtectionSystem(existAsset, updateAsset);
            case APPLICATION_PROGRAM -> updateApplicationProgram(existAsset, updateAsset);
            case SOFTWARE -> updateSoftware(existAsset, updateAsset);
            case ELECTRONIC_INFORMATION -> updateElectronicInformation(existAsset, updateAsset);
            case DOCUMENT -> updateDocument(existAsset, updateAsset);
            case PATENTS_AND_TRADEMARKS -> updatePatentsAndTrademarks(existAsset, updateAsset);
            case ITSYSTEM_EQUIPMENT -> updateItSystemEquipment(existAsset, updateAsset);
            case ITNETWORK_EQUIPMENT -> updateItNetworkEquipment(existAsset, updateAsset);
            case TERMINAL -> updateTerminal(existAsset, updateAsset);
            case FURNITURE -> updateFurniture(existAsset, updateAsset);
            case DEVICES -> updateDevices(existAsset, updateAsset);
            case CAR -> updateCar(existAsset, updateAsset);
            case OTHERASSETS -> updateOtherAssets(existAsset, updateAsset);
            default -> throw new IllegalArgumentException("Unknown asset classification: " + updateAsset.getAssetClassification());
        }
    }

    private void updateInformationProtectionSystem(CommonAsset existAsset, CommonAsset updateAsset) {
        InformationProtectionSystemDto informationProtectionSystemDto = InformationProtectionSystemDto.fromEntity(informationProtectionSystemRepository.findByAssetNo(existAsset));
        informationProtectionSystemDto.setInfoNo(null);
        informationProtectionSystemDto.setAssetNo(updateAsset);
        informationProtectionSystemRepository.save(informationProtectionSystemDto.toEntity());
    }

    private void updateApplicationProgram(CommonAsset existAsset, CommonAsset updateAsset) {
        ApplicationProgramDto appProgramDto = ApplicationProgramDto.fromEntity(applicationProgramRepository.findByAssetNo(existAsset));
        appProgramDto.setAppNo(null); // ID를 null로 설정
        appProgramDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        applicationProgramRepository.save(appProgramDto.toEntity()); // 저장
    }

    private void updateSoftware(CommonAsset existAsset, CommonAsset updateAsset) {
        SoftwareDto softwareDto = SoftwareDto.fromEntity(softwareRepository.findByAssetNo(existAsset));
        softwareDto.setSoftwareNo(null); // ID를 null로 설정
        softwareDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        softwareRepository.save(softwareDto.toEntity()); // 저장
    }

    private void updateElectronicInformation(CommonAsset existAsset, CommonAsset updateAsset) {
        ElectronicInformationDto elecInfoDto = ElectronicInformationDto.fromEntity(electronicInformationRepository.findByAssetNo(existAsset));
        elecInfoDto.setEInfoNo(null); // ID를 null로 설정
        elecInfoDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        electronicInformationRepository.save(elecInfoDto.toEntity()); // 저장
    }

    private void updateDocument(CommonAsset existAsset, CommonAsset updateAsset) {
        DocumentDto documentDto = DocumentDto.fromEntity(documentRepository.findByAssetNo(existAsset));
        documentDto.setDocumentNo(null); // ID를 null로 설정
        documentDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        documentRepository.save(documentDto.toEntity()); // 저장
    }

    private void updatePatentsAndTrademarks(CommonAsset existAsset, CommonAsset updateAsset) {
        PatentsAndTrademarksDto patentsDto = PatentsAndTrademarksDto.fromEntity(patentsAndTrademarksRepository.findByAssetNo(existAsset));
        patentsDto.setPatentTrademarkNo(null); // ID를 null로 설정
        patentsDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        patentsAndTrademarksRepository.save(patentsDto.toEntity()); // 저장
    }

    private void updateItSystemEquipment(CommonAsset existAsset, CommonAsset updateAsset) {
        ItSystemEquipmentDto itEquipmentDto = ItSystemEquipmentDto.fromEntity(itSystemEquipmentRepository.findByAssetNo(existAsset));
        itEquipmentDto.setEquipmentNo(null); // ID를 null로 설정
        itEquipmentDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        itSystemEquipmentRepository.save(itEquipmentDto.toEntity()); // 저장
    }

    private void updateItNetworkEquipment(CommonAsset existAsset, CommonAsset updateAsset) {
        ItNetworkEquipmentDto networkEquipmentDto = ItNetworkEquipmentDto.fromEntity(itNetworkEquipmentRepository.findByAssetNo(existAsset));
        networkEquipmentDto.setNetworkNo(null); // ID를 null로 설정
        networkEquipmentDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        itNetworkEquipmentRepository.save(networkEquipmentDto.toEntity()); // 저장
    }

    private void updateTerminal(CommonAsset existAsset, CommonAsset updateAsset) {
        TerminalDto terminalDto = TerminalDto.fromEntity(terminalRepository.findByAssetNo(existAsset));
        terminalDto.setTerminalNo(null); // ID를 null로 설정
        terminalDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        terminalRepository.save(terminalDto.toEntity()); // 저장
    }

    private void updateFurniture(CommonAsset existAsset, CommonAsset updateAsset) {
        FurnitureDto furnitureDto = FurnitureDto.fromEntity(furnitureRepository.findByAssetNo(existAsset));
        furnitureDto.setFurnitureNo(null); // ID를 null로 설정
        furnitureDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        furnitureRepository.save(furnitureDto.toEntity()); // 저장
    }

    private void updateDevices(CommonAsset existAsset, CommonAsset updateAsset) {
        DevicesDto devicesDto = DevicesDto.fromEntity(devicesRepository.findByAssetNo(existAsset));
        devicesDto.setDeviceNo(null); // ID를 null로 설정
        devicesDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        devicesRepository.save(devicesDto.toEntity()); // 저장
    }

    private void updateCar(CommonAsset existAsset, CommonAsset updateAsset) {
        CarDto carDto = CarDto.fromEntity(carRepository.findByAssetNo(existAsset));
        carDto.setCarNo(null); // ID를 null로 설정
        carDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        carRepository.save(carDto.toEntity()); // 저장
    }

    private void updateOtherAssets(CommonAsset existAsset, CommonAsset updateAsset) {
        OtherAssetsDto otherAssetsDto = OtherAssetsDto.fromEntity(otherAssetsRepository.findByAssetNo(existAsset));
        otherAssetsDto.setOtherNo(null); // ID를 null로 설정
        otherAssetsDto.setAssetNo(updateAsset); // 새로운 자산 정보 설정
        otherAssetsRepository.save(otherAssetsDto.toEntity()); // 저장
    }

}
