package com.codehows.taelim.service;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.AssetDisposeDto;
import com.codehows.taelim.dto.AssetUpdateDto;
import com.codehows.taelim.dto.ExcelDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
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
    private final FileRepository fileRepository;
    @Value("${file.path}")
    private String filePath;

    @Value("${file.url}")
    private String fileUrl;

    //자산 등록
    public Long assetRegister(AssetDto assetDto){

//        Member assetUser =memberRepository.findByEmail(assetDto.getAssetUser());

//        Member assetOwner = memberRepository.findByEmail(assetDto.getAssetOwner());
//        Member assetSecurityManager = memberRepository.findByEmail(assetDto.getAssetSecurityManager());
        CommonAsset commonAsset = assetDto.toEntity();
//        commonAsset.setAssetUser(assetUser);
//        commonAsset.setAssetOwner(assetUser);
//       commonAsset.setAssetSecurityManager(assetUser);
        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.FALSE);
        commonAsset.setDemandStatus(Boolean.FALSE);
        commonAsset.setDemandCheck(Boolean.FALSE);
        commonAsset.setCreateDate(LocalDate.now());

        if(commonAsset.getPurchaseCost() >= 1000000 && commonAsset.getPurchaseCost() < 100000000) {
            commonAsset.setConfidentiality(2);
            commonAsset.setIntegrity(2);
            commonAsset.setAvailability(2);
        }else if(commonAsset.getPurchaseCost() >= 100000000){
            commonAsset.setConfidentiality(3);
            commonAsset.setIntegrity(3);
            commonAsset.setAvailability(3);
        }else {
            commonAsset.setConfidentiality(1);
            commonAsset.setIntegrity(1);
            commonAsset.setAvailability(1);
        }

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


    //엑셀로 등록
    public void excelRegister (AssetDto assetDto) {

//        Member assetOwner = memberRepository.findByUName(excelDto.getAssetOwner())
//                .orElse(new Member(excelDto.getAssetOwner()));
//        Member assetUser = memberRepository.findByUName(excelDto.getAssetUser())
//                .orElse(new Member(excelDto.getAssetUser()));
//        Member assetSecurityManager = memberRepository.findByUName(excelDto.getAssetSecurityManager())
//                .orElse(new Member(excelDto.getAssetSecurityManager()));
        CommonAsset commonAsset = assetDto.toEntity();
//        commonAsset.setAssetOwner(assetOwner);
//        commonAsset.setAssetUser(assetUser);

        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.FALSE);
        commonAsset.setDemandStatus(Boolean.FALSE);
        commonAsset.setDemandCheck(Boolean.FALSE);
        commonAsset.setCreateDate(LocalDate.now());
//        commonAsset.setAssetSecurityManager(assetSecurityManager);
        System.out.println("excel commonAsset : " + commonAsset);
        String assetCode = generateAssetCode(commonAsset.getAssetClassification());
        commonAsset.setAssetCode(assetCode);

        CommonAsset commonAsset1 = commonAssetRepository.save(commonAsset);

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

    }
    public void excelRegisterAll(List<AssetDto> excelDtos) {

        for (AssetDto excelDto : excelDtos) {
            excelRegister(excelDto);
            System.out.println(excelDto);
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
    public String generateAssetCode(AssetClassification classification) {
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

    public AssetUpdateResponse updateAssetCode(String assetCode, AssetUpdateDto assetDto) {

        // 기존 입력되어있는 assetCode 조회
        CommonAsset existAsset = commonAssetRepository.findLatestAssetCode(assetCode)
                .orElseThrow(() -> new RuntimeException("자산코드를 찾을수 없음 " + assetCode));

        // 자산 상태가 수정요청일때 Unconfirmed인지 확인
        if (existAsset.getApproval() == Approval.UNCONFIRMED) {
            // 자산 상태가 UNCONFIRMED이면 수정 요청 불가 메시지 반환
            return new AssetUpdateResponse("이미 수정 요청이 들어간 자산입니다.", null);
        }

        // 자산 상태가 폐기 요청일 떄 Uncofirmed인지 확인
        else if (existAsset.getApproval() == Approval.UNCONFIRMED && existAsset.getDisposalStatus() == Boolean.TRUE) {
            //
            return new AssetUpdateResponse( "이미 폐기 요청이 들어간 자산입니다.", null);
        }
        else if (existAsset.getApproval() == Approval.REFUSAL || existAsset.getApproval() == Approval.APPROVE) {

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
        updateAsset.setOwnership(assetDto.getOwnership() != null ? assetDto.getOwnership() : existAsset.getOwnership()); // 소유 enum 추가
        updateAsset.setQuantity(assetDto.getQuantity() != null ? assetDto.getQuantity() : existAsset.getQuantity());  // 수량 추가
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
        updateAsset.setAttachment(assetDto.getAttachment() != null ? assetDto.getAttachment() : existAsset.getAttachment()); // 첨부파일 추가
        updateAsset.setWarrantyDetails(assetDto.getWarrantyDetails() != null ? assetDto.getWarrantyDetails() : existAsset.getWarrantyDetails()); // 보증서 추사

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

        //자산 수정 성공 메시지 반환
        return new AssetUpdateResponse("자산 수정 완료 : " + newAssetNo, newAssetNo);
        }

        // 상태가 다를 경우 기본 응답 (추가할 상태가 있으면 여기서 처리 가능)
        return new AssetUpdateResponse("알 수 없는 자산 상태입니다.", null);
        //return newAssetNo;
    }

    public AssetUpdateResponse updatedemandAssetCode(String assetCode, AssetUpdateDto assetDto) {

        // 기존 입력되어있는 assetCode 조회
        CommonAsset existAsset = commonAssetRepository.findLatestAssetCode(assetCode)
                .orElseThrow(() -> new RuntimeException("자산코드를 찾을수 없음 " + assetCode));

        // 자산 상태가 수정요청일때 Unconfirmed인지 확인
        if (existAsset.getApproval() == Approval.UNCONFIRMED) {
            // 자산 상태가 UNCONFIRMED이면 수정 요청 불가 메시지 반환
            return new AssetUpdateResponse("이미 수정 요청이 들어간 자산입니다.", null);
        }

        // 자산 상태가 폐기 요청일 떄 Uncofirmed인지 확인
        else if (existAsset.getApproval() == Approval.UNCONFIRMED && existAsset.getDisposalStatus() == Boolean.TRUE) {
            //
            return new AssetUpdateResponse( "이미 폐기 요청이 들어간 자산입니다.", null);
        }

        else if (existAsset.getApproval() == Approval.REFUSAL || existAsset.getApproval() == Approval.APPROVE) {
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
        updateAsset.setOwnership(assetDto.getOwnership() != null ? assetDto.getOwnership() : existAsset.getOwnership()); // 소유 enum 추가
        updateAsset.setQuantity(assetDto.getQuantity() != null ? assetDto.getQuantity() : existAsset.getQuantity());  // 수량 추가
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
        updateAsset.setAttachment(assetDto.getAttachment() != null ? assetDto.getAttachment() : existAsset.getAttachment()); // 첨부파일 추가
        updateAsset.setWarrantyDetails(assetDto.getWarrantyDetails() != null ? assetDto.getWarrantyDetails() : existAsset.getWarrantyDetails()); // 보증서 추사

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

        //파일 복사
        List<FileDto> files = assetDto.getFiles();
        for (FileDto fileDto : files) {
            File file = new File();
            file.setAssetNo(updateAsset);

            // 기존 파일 이름에서 확장자 추출
            String originalFileName = fileDto.getOriFileName();
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

            // 새로운 UUID로 파일 이름 생성
            String uuid = UUID.randomUUID().toString();
            String saveFileName = uuid + extension;
            String savePath = filePath + saveFileName;

            // 새로운 URL 생성
            String url = fileUrl + saveFileName; // 저장한 파일의 URL 생성
            file.setFileURL(url);
            file.setOriFileName(originalFileName);
            file.setFileName(saveFileName);
            file.setFileExt(extension);
            file.setFileSize(fileDto.getFileSize()); // 기존 파일 크기 사용
            file.setFileType(fileDto.getFileType()); // 기존 파일 타입 사용

            // 실제 파일 저장 경로 생성
            try {
                java.io.File dir = new java.io.File(filePath);  // filePath 에 해당하는 경로를 File 객체로 생성
                if (!dir.exists()) {
                    dir.mkdirs(); // 경로가 존재하지 않으면 생성
                }

                // 기존 URL에서 파일 이름 추출
                String existingFileName = fileDto.getFileURL().substring(fileDto.getFileURL().lastIndexOf('/') + 1);
                java.io.File existingFile = new java.io.File(filePath + existingFileName); // 기존 파일 경로

                // 파일 복사
                try (FileInputStream fis = new FileInputStream(existingFile);
                     FileOutputStream fos = new FileOutputStream(savePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // 예외 처리
                    continue; // 다음 파일로 진행
                }

            } catch (Exception e) {
                e.printStackTrace(); // 예외 처리
                continue; // 다음 파일로 진행
            }

            // 파일 엔티티 저장
            fileRepository.save(file);
        }

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

        // 자산 수정 성공 메시지 반환
        return new AssetUpdateResponse("자산 수정 등록 완료 : " + newAssetNo, newAssetNo);
        }
        // 상태가 다를 경우 기본 응답 (추가할 상태가 있으면 여기서 처리 가능)
        return new AssetUpdateResponse("알 수 없는 자산 상태입니다.", null);
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

        //파일 복사
        List<FileDto> files = allUpdateDto.getAssetDto().getFiles();
        for (FileDto fileDto : files) {
            File file = new File();
            file.setAssetNo(updateAsset);

            // 기존 파일 이름에서 확장자 추출
            String originalFileName = fileDto.getOriFileName();
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

            // 새로운 UUID로 파일 이름 생성
            String uuid = UUID.randomUUID().toString();
            String saveFileName = uuid + extension;
            String savePath = filePath + saveFileName;

            // 새로운 URL 생성
            String url = fileUrl + saveFileName; // 저장한 파일의 URL 생성
            file.setFileURL(url);
            file.setOriFileName(originalFileName);
            file.setFileName(saveFileName);
            file.setFileExt(extension);
            file.setFileSize(fileDto.getFileSize()); // 기존 파일 크기 사용
            file.setFileType(fileDto.getFileType()); // 기존 파일 타입 사용

            // 실제 파일 저장 경로 생성
            try {
                java.io.File dir = new java.io.File(filePath);  // filePath 에 해당하는 경로를 File 객체로 생성
                if (!dir.exists()) {
                    dir.mkdirs(); // 경로가 존재하지 않으면 생성
                }

                // 기존 URL에서 파일 이름 추출
                String existingFileName = fileDto.getFileURL().substring(fileDto.getFileURL().lastIndexOf('/') + 1);
                java.io.File existingFile = new java.io.File(filePath + existingFileName); // 기존 파일 경로

                // 파일 복사
                try (FileInputStream fis = new FileInputStream(existingFile);
                     FileOutputStream fos = new FileOutputStream(savePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // 예외 처리
                    continue; // 다음 파일로 진행
                }

            } catch (Exception e) {
                e.printStackTrace(); // 예외 처리
                continue; // 다음 파일로 진행
            }

            // 파일 엔티티 저장
            fileRepository.save(file);
        }

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

        //파일 복사
        List<FileDto> files = allUpdateDto.getAssetDto().getFiles();
        for (FileDto fileDto : files) {
            File file = new File();
            file.setAssetNo(updateAsset);

            // 기존 파일 이름에서 확장자 추출
            String originalFileName = fileDto.getOriFileName();
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

            // 새로운 UUID로 파일 이름 생성
            String uuid = UUID.randomUUID().toString();
            String saveFileName = uuid + extension;
            String savePath = filePath + saveFileName;

            // 새로운 URL 생성
            String url = fileUrl + saveFileName; // 저장한 파일의 URL 생성
            file.setFileURL(url);
            file.setOriFileName(originalFileName);
            file.setFileName(saveFileName);
            file.setFileExt(extension);
            file.setFileSize(fileDto.getFileSize()); // 기존 파일 크기 사용
            file.setFileType(fileDto.getFileType()); // 기존 파일 타입 사용

            // 실제 파일 저장 경로 생성
            try {
                java.io.File dir = new java.io.File(filePath);  // filePath 에 해당하는 경로를 File 객체로 생성
                if (!dir.exists()) {
                    dir.mkdirs(); // 경로가 존재하지 않으면 생성
                }

                // 기존 URL에서 파일 이름 추출
                String existingFileName = fileDto.getFileURL().substring(fileDto.getFileURL().lastIndexOf('/') + 1);
                java.io.File existingFile = new java.io.File(filePath + existingFileName); // 기존 파일 경로

                // 파일 복사
                try (FileInputStream fis = new FileInputStream(existingFile);
                     FileOutputStream fos = new FileOutputStream(savePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // 예외 처리
                    continue; // 다음 파일로 진행
                }

            } catch (Exception e) {
                e.printStackTrace(); // 예외 처리
                continue; // 다음 파일로 진행
            }

            // 파일 엔티티 저장
            fileRepository.save(file);
        }

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

        //파일 복사
        List<FileDto> files = allDeleteDto.getAssetDto().getFiles();
        for (FileDto fileDto : files) {
            File file = new File();
            file.setAssetNo(updateAsset);
            file.setFileName(fileDto.getFileName());
            file.setFileExt(fileDto.getFileExt());
            file.setFileSize(fileDto.getFileSize());
            file.setFileType(fileDto.getFileType());
            file.setFileURL(fileDto.getFileURL());
            file.setOriFileName(fileDto.getOriFileName());
            fileRepository.save(file);
        }

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

        //파일 복사
        List<FileDto> files = allDeleteDto.getAssetDto().getFiles();
        for (FileDto fileDto : files) {
            File file = new File();
            file.setAssetNo(updateAsset);
            file.setFileName(fileDto.getFileName());
            file.setFileExt(fileDto.getFileExt());
            file.setFileSize(fileDto.getFileSize());
            file.setFileType(fileDto.getFileType());
            file.setFileURL(fileDto.getFileURL());
            file.setOriFileName(fileDto.getOriFileName());
            fileRepository.save(file);
        }

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

    // 파일 수정 및 등록 서비스 메서드
    public void updateAssetFiles(String assetCode, List<MultipartFile> newFiles, FileType fileType) {
        // 1. assetCode로 CommonAsset 조회
        Optional<CommonAsset> optionalAsset = commonAssetRepository.findByAssetCode(assetCode);

        // 2. Asset이 존재하지 않을 경우 처리
        CommonAsset asset = optionalAsset.orElseThrow(() ->
                new IllegalArgumentException("해당 assetCode에 대한 자산을 찾을 수 없습니다: " + assetCode));

        // 3. 기존 파일 정보 조회 (assetCode로 파일 리스트를 가져옴)
        List<File> existingFiles = fileRepository.findByAssetCode(assetCode);

        // 4. 새롭게 사용할 FileDto 리스트 생성
        List<FileDto> updatedFileDtos = new ArrayList<>();

        // 5. 기존 파일을 Map으로 변환하여 최신 파일 관리
        Map<FileType, File> latestFilesMap = new HashMap<>();
        for (File existingFile : existingFiles) {
            latestFilesMap.put(existingFile.getFileType(), existingFile);
        }

        // 6. 새롭게 추가할 파일 정보 처리
        for (MultipartFile newFile : newFiles) {
            // 원본 파일 이름 가져오기
            String originalFileName = newFile.getOriginalFilename();

            // 확장자 가져오기
            String extension = originalFileName != null && originalFileName.contains(".") ?
                    originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

            // UUID 생성 및 파일 저장 경로 설정
            String uuid = UUID.randomUUID().toString();
            String saveFileName = uuid + extension;
            String url = fileUrl + saveFileName; // fileUrl은 파일 접근 URL
            String savePath = filePath + saveFileName; // c드라이브 fileUpload에 저장

            // 새롭게 추가된 파일에 대한 FileDto 생성
            FileDto newFileDto = new FileDto();
            newFileDto.setAssetNo(asset.getAssetNo());
            newFileDto.setFileNo(null); // 새 파일이기 때문에 fileNo는 null
            newFileDto.setFileName(saveFileName);
            newFileDto.setOriFileName(originalFileName);
            newFileDto.setFileSize(newFile.getSize()); // 파일의 실제 크기 설정
            newFileDto.setFileExt(extension);
            newFileDto.setFileURL(url);
            newFileDto.setFileType(fileType);

            // 기존 파일과 새 파일을 비교하여 최신 파일 유지
            if (!latestFilesMap.containsKey(fileType) ||
                    (newFileDto.getFileNo() == null && !existingFiles.isEmpty())) { // 새 파일이 존재하고 기존 파일이 있는 경우
                updatedFileDtos.add(newFileDto);
            } else {
                // 기존 파일이 있는 경우 최신 파일 비교
                File existingFile = latestFilesMap.get(fileType);
                // fileNo를 비교하여 최신 파일 결정
                if (existingFile.getFileNo() > (newFileDto.getFileNo() == null ? -1 : newFileDto.getFileNo())) {
                    // 기존 파일이 최신인 경우
                    FileDto existingFileDto = new FileDto();
                    existingFileDto.setAssetNo(existingFile.getAssetNo().getAssetNo());
                    existingFileDto.setFileNo(existingFile.getFileNo());
                    existingFileDto.setOriFileName(existingFile.getOriFileName());
                    existingFileDto.setFileName(existingFile.getFileName());
                    existingFileDto.setFileSize(existingFile.getFileSize());
                    existingFileDto.setFileExt(existingFile.getFileExt());
                    existingFileDto.setFileURL(existingFile.getFileURL());
                    existingFileDto.setFileType(existingFile.getFileType());

                    updatedFileDtos.add(existingFileDto);
                } else {
                    // 새 파일이 최신인 경우
                    updatedFileDtos.add(newFileDto);
                }
            }

            // 파일 저장
            java.io.File saveFile = new java.io.File(savePath); // Java File 클래스를 사용하여 저장
            try {
                newFile.transferTo(saveFile); // 파일을 지정한 경로에 저장
            } catch (IOException e) {
                e.printStackTrace(); // 에러 로그 출력
            }
        }

        // 7. 업데이트된 파일 리스트 저장
        List<File> filesToSave = updatedFileDtos.stream()
                .map(this::convertToFile) // FileDto를 File로 변환
                .collect(Collectors.toList());

        System.out.println("Number of files to save: " + filesToSave.size()); // 저장할 파일 개수 로그 출력

        try {
            fileRepository.saveAll(filesToSave);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
        }
    }



    // FileDto를 File로 변환하는 메서드
    private File convertToFile(FileDto fileDto) {
        File file = new File();
        file.setFileNo(fileDto.getFileNo());

        // assetNo 설정
        CommonAsset asset = commonAssetRepository.findById(fileDto.getAssetNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 Asset을 찾을 수 없습니다: " + fileDto.getAssetNo()));
        file.setAssetNo(asset); // assetNo를 FK로 설정

        file.setOriFileName(fileDto.getOriFileName());
        file.setFileName(fileDto.getFileName());
        file.setFileSize(fileDto.getFileSize());
        file.setFileExt(fileDto.getFileExt());
        file.setFileURL(fileDto.getFileURL());
        file.setFileType(fileDto.getFileType());

        return file;
    }


    // 파일 확장자 가져오기
    private String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf('.'));
        }
        return "";
    }
}


