package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final CommonAssetRepository commonAssetRepository;
    private final InformationProtectionSystemRepository informationProtectionSystemRepository;
    private final ApplicationProgramRepository applicationProgramRepository;
    private final MemberRepository memberRepository;
    private final SoftwareRepository softwareRepository;
    private final ElectronicInformationRepository electronicInformationRepository;
    private final DocumentRepository documentRepository;
    private final PatentsAndTrademarksRepository patentsAndTrademarksRepository;
    private final ItSystemEquipmentRepository itSystemEquipmentRepository;
    private final ItNetworkEquipmentRepository itNetworkEquipmentRepository;
    private final TerminalRepository terminalRepository;
    private final FurnitureRepository furnitureRepository;
    private final DevicesRepository devicesRepository;
    private final CarRepository carRepository;
    private final OtherAssetsRepository otherAssetsRepository;
    public void assetRegister(AssetDto assetDto){

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
}
