package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.dto.ExcelDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //자산 등록
    public Long assetRegister(AssetDto assetDto){

        Member assetUser = memberRepository.findByEmail(assetDto.getAssetUser());
        Member assetOwner = memberRepository.findByEmail(assetDto.getAssetOwner());
        Member assetSecurityManager = memberRepository.findByEmail(assetDto.getAssetSecurityManager());
        CommonAsset commonAsset = assetDto.toEntity();
        commonAsset.setAssetUser(assetUser);
        commonAsset.setAssetOwner(assetOwner);
        commonAsset.setAssetSecurityManager(assetSecurityManager);
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

    public Long updateAssetCode(String assetCode, AssetDto assetDto) {

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

        return newAssetNo;
    }

    public Long updatedemandAssetCode(String assetCode, AssetDto assetDto) {

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

        return newAssetNo;
    }
    private void saveRelatedEntity(AssetDto assetDto, CommonAsset latestAsset) {
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
}
