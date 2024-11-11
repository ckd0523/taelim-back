package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.dto.FileDto;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AssetService {

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
    private final FileRepository fileRepository;
    private final RepairHistoryRepository repairHistoryRepository;
    private final AssetSurveyService assetSurveyService;
    private final DemandRepository demandRepository;
    private final DemandDtlRepository demandDtlRepository;
    private final EmailServcie emailService;
    private final DemandService demandService;
    private final AssetSurveyDetailRepository assetSurveyDetailRepository;
    private final UserService userService;


    // 자산 상세 조회2
    public Map<String, Object> getAssetDetail2(String assetCode) {

        Map<String, Object> result = new HashMap<>();
        CommonAssetDto assetDto = new CommonAssetDto();
        Optional<CommonAsset> commonAssetObj = commonAssetRepository.findLatestApprovedAsset(assetCode);
        CommonAsset commonAsset = commonAssetObj.get();
        String assetOwnerFullname = userService.getUserById(commonAsset.getAssetOwner()) != null
                ? userService.getUserById(commonAsset.getAssetOwner()).getFullname()
                : "Unknown Owner";

        String assetUserFullname = userService.getUserById(commonAsset.getAssetUser()) != null
                ? userService.getUserById(commonAsset.getAssetUser()).getFullname()
                : "Unknown User";

        String assetSecurityManagerFullname = userService.getUserById(commonAsset.getAssetSecurityManager()) != null
                ? userService.getUserById(commonAsset.getAssetSecurityManager()).getFullname()
                : "Unknown Security Manager";
        assetDto.setAssetNo(commonAsset.getAssetNo());
        assetDto.setAssetBasis(commonAsset.getAssetBasis());
        assetDto.setAssetCode(commonAsset.getAssetCode());
        assetDto.setAssetClassification(commonAsset.getAssetClassification());
        assetDto.setAssetName(commonAsset.getAssetName());
        assetDto.setAssetOwner(assetOwnerFullname);
        assetDto.setAssetUser(assetUserFullname);
        assetDto.setAssetSecurityManager(assetSecurityManagerFullname);
        assetDto.setManufacturingCompany(commonAsset.getManufacturingCompany());
        assetDto.setPurpose(commonAsset.getPurpose());
        assetDto.setDepartment(commonAsset.getDepartment());
        assetDto.setAssetLocation(commonAsset.getAssetLocation());
        assetDto.setUseStated(commonAsset.getUseStated());
        assetDto.setOperationStatus(commonAsset.getOperationStatus());
        assetDto.setIntroducedDate(commonAsset.getIntroducedDate());
        assetDto.setQuantity(commonAsset.getQuantity());
        assetDto.setOwnership(commonAsset.getOwnership());
        assetDto.setConfidentiality(commonAsset.getConfidentiality());
        assetDto.setIntegrity(commonAsset.getIntegrity());
        assetDto.setAvailability(commonAsset.getAvailability());
        assetDto.setNote(commonAsset.getNote());
        assetDto.setPurchaseCost(commonAsset.getPurchaseCost());
        assetDto.setPurchaseDate(commonAsset.getPurchaseDate());
        assetDto.setUsefulLife(commonAsset.getUsefulLife());
        assetDto.setDepreciationMethod(commonAsset.getDepreciationMethod());
        assetDto.setPurchaseSource(commonAsset.getPurchaseSource());
        assetDto.setContactInformation(commonAsset.getContactInformation());
        assetDto.setAcquisitionRoute(commonAsset.getAcquisitionRoute());
        assetDto.setMaintenancePeriod(commonAsset.getMaintenancePeriod());
        assetDto.setWarrantyDetails(commonAsset.getWarrantyDetails());
        assetDto.setAttachment(commonAsset.getAttachment());
        assetDto.setDisposalStatus(commonAsset.getDisposalStatus());
        assetDto.setDemandStatus(commonAsset.getDemandStatus());
        assetDto.setApproval(commonAsset.getApproval());
        assetDto.setDemandCheck(commonAsset.getDemandCheck());
        assetDto.setCreateDate(commonAsset.getCreateDate());

        AssetClassification AssetClassification = commonAsset.getAssetClassification();
        switch (AssetClassification) {
            case SOFTWARE -> {
                Software software = softwareRepository.findByAssetNo(commonAsset);
                SoftwareDto softwareDto = new SoftwareDto();
                softwareDto.setCompanyManager(software.getCompanyManager());
                softwareDto.setIp(software.getIp());
                softwareDto.setOs(software.getOs());
                softwareDto.setServerId(software.getServerId());
                softwareDto.setServerPassword(software.getServerPassword());
                result.put("softwareDto", softwareDto);
            }
            case CAR -> {
                Car car = carRepository.findByAssetNo(commonAsset);
                CarDto carDto = new CarDto();
                carDto.setDisplacement(car.getDisplacement());
                carDto.setDoorsCount(car.getDoorsCount());
                carDto.setEngineType(car.getEngineType());
                carDto.setCarType(car.getCarType());
                carDto.setIdentificationNo(car.getIdentificationNo());
                carDto.setCarColor(car.getCarColor());
                carDto.setModelYear(car.getModelYear());
                result.put("carDto", carDto);
            }
            case DEVICES -> {

                Devices devices = devicesRepository.findByAssetNo(commonAsset);
                DevicesDto devicesDto = new DevicesDto();
                devicesDto.setDeviceType(devices.getDeviceType());
                devicesDto.setModelNumber(devices.getModelNumber());
                devicesDto.setConnectionType(devices.getConnectionType());
                devicesDto.setPowerSpecifications(devices.getPowerSpecifications());
                result.put("devicesDto", devicesDto);

            }
            case DOCUMENT -> {

                Document document = documentRepository.findByAssetNo(commonAsset);
                DocumentDto documentDto = new DocumentDto();
                documentDto.setDocumentGrade(document.getDocumentGrade());
                documentDto.setDocumentType(document.getDocumentType());
                documentDto.setDocumentLink(document.getDocumentLink());
                result.put("documentDto", documentDto);
            }
            case TERMINAL -> {
                Terminal terminal = terminalRepository.findByAssetNo(commonAsset);
                TerminalDto terminalDto = new TerminalDto();
                terminalDto.setIp(terminal.getIp());
                terminalDto.setOs(terminal.getOs());
                terminalDto.setSecurityControl(terminal.getSecurityControl());
                terminalDto.setKaitsKeeper(terminal.getKaitsKeeper());
                terminalDto.setV3OfficeSecurity(terminal.getV3OfficeSecurity());
                terminalDto.setAppCheckPro(terminal.getAppCheckPro());
                terminalDto.setTgate(terminal.getTgate());
                result.put("terminalDto", terminalDto);

            }
            case FURNITURE -> {
                Furniture furniture = furnitureRepository.findByAssetNo(commonAsset);
                FurnitureDto furnitureDto = new FurnitureDto();
                furnitureDto.setFurnitureSize(furniture.getFurnitureSize());
                result.put("furnitureDto", furnitureDto);

            }
            case OTHERASSETS -> {
                OtherAssets otherAssets = otherAssetsRepository.findByAssetNo(commonAsset);
                OtherAssetsDto otherAssetsDto = new OtherAssetsDto();
                otherAssetsDto.setOtherDescription(otherAssets.getOtherDescription());
                otherAssetsDto.setUsageFrequency(otherAssets.getUsageFrequency());
                result.put("otherAssetsDto", otherAssetsDto);
            }
            case ITSYSTEM_EQUIPMENT -> {
                ItSystemEquipment itSystemEquipment = itSystemEquipmentRepository.findByAssetNo(commonAsset);
                ItSystemEquipmentDto itSystemEquipmentDto = new ItSystemEquipmentDto();
                itSystemEquipmentDto.setEquipmentType(itSystemEquipment.getEquipmentType());
                itSystemEquipmentDto.setPowerSupply(itSystemEquipment.getPowerSupply());
                itSystemEquipmentDto.setCoolingSystem(itSystemEquipment.getCoolingSystem());
                itSystemEquipmentDto.setInterfacePorts(itSystemEquipment.getInterfacePorts());
                itSystemEquipmentDto.setFormFactor(itSystemEquipment.getFormFactor());
                itSystemEquipmentDto.setExpansionSlots(itSystemEquipment.getExpansionSlots());
                itSystemEquipmentDto.setGraphicsCard(itSystemEquipment.getGraphicsCard());
                itSystemEquipmentDto.setPortConfiguration(itSystemEquipment.getPortConfiguration());
                itSystemEquipmentDto.setMonitorIncluded(itSystemEquipment.getMonitorIncluded());
                result.put("itSystemEquipmentDto", itSystemEquipmentDto);

            }
            case APPLICATION_PROGRAM -> {
                ApplicationProgram applicationProgram = applicationProgramRepository.findByAssetNo(commonAsset);
                ApplicationProgramDto applicationProgramDto = new ApplicationProgramDto();
                applicationProgramDto.setServiceScope(applicationProgram.getServiceScope());
                applicationProgramDto.setOs(applicationProgram.getOs());
                applicationProgramDto.setRelatedDB(applicationProgram.getRelatedDB());
                applicationProgramDto.setIp(applicationProgram.getIp());
                applicationProgramDto.setScreenNumber(applicationProgram.getScreenNumber());
                result.put("applicationProgramDto", applicationProgramDto);
            }
            case ITNETWORK_EQUIPMENT -> {
                ItNetworkEquipment itNetworkEquipment = itNetworkEquipmentRepository.findByAssetNo(commonAsset);
                ItNetworkEquipmentDto itNetworkEquipmentDto = new ItNetworkEquipmentDto();
                itNetworkEquipmentDto.setEquipmentType(itNetworkEquipment.getEquipmentType());
                itNetworkEquipmentDto.setNumberOfPorts(itNetworkEquipment.getNumberOfPorts());
                itNetworkEquipmentDto.setSupportedProtocols(itNetworkEquipment.getSupportedProtocols());
                itNetworkEquipmentDto.setFirmwareVersion(itNetworkEquipment.getFirmwareVersion());
                itNetworkEquipmentDto.setNetworkSpeed(itNetworkEquipment.getNetworkSpeed());
                itNetworkEquipmentDto.setServiceScope(itNetworkEquipment.getServiceScope());
                result.put("itNetworkEquipmentDto", itNetworkEquipmentDto);
            }
            case ELECTRONIC_INFORMATION -> {
                ElectronicInformation electronicInformation = electronicInformationRepository.findByAssetNo(commonAsset);
                ElectronicInformationDto electronicInformationDto = new ElectronicInformationDto();
                electronicInformationDto.setOs(electronicInformation.getOs());
                electronicInformationDto.setSystem(electronicInformation.getSystem());
                electronicInformationDto.setDBType(electronicInformation.getDbtype());
                result.put("electronicInformationDto", electronicInformationDto);
            }
            case PATENTS_AND_TRADEMARKS -> {
                PatentsAndTrademarks patentsAndTrademarks = patentsAndTrademarksRepository.findByAssetNo(commonAsset);
                PatentsAndTrademarksDto patentsAndTrademarksDto = new PatentsAndTrademarksDto();
                patentsAndTrademarksDto.setApplicationDate(patentsAndTrademarks.getApplicationDate());
                patentsAndTrademarksDto.setRegistrationDate(patentsAndTrademarks.getRegistrationDate());
                patentsAndTrademarksDto.setExpirationDate(patentsAndTrademarks.getExpirationDate());
                patentsAndTrademarksDto.setPatentTrademarkStatus(patentsAndTrademarks.getPatentTrademarkStatus());
                patentsAndTrademarksDto.setCountryApplication(patentsAndTrademarks.getCountryApplication());
                patentsAndTrademarksDto.setPatentClassification(patentsAndTrademarks.getPatentClassification());
                patentsAndTrademarksDto.setPatentItem(patentsAndTrademarks.getPatentItem());
                patentsAndTrademarksDto.setApplicationNo(patentsAndTrademarks.getApplicationNo());
                patentsAndTrademarksDto.setInventor(patentsAndTrademarks.getInventor());
                patentsAndTrademarksDto.setAssignee(patentsAndTrademarks.getAssignee());
                result.put("patentsAndTrademarks", patentsAndTrademarksDto);
            }
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(commonAsset);
                InformationProtectionSystemDto informationProtectionSystemDto = new InformationProtectionSystemDto();
                informationProtectionSystemDto.setServiceScope(informationProtectionSystem.getServiceScope());
                result.put("informationProtectionSystemDto", informationProtectionSystemDto);
            }

        }

        //파일
        List<File> fileList = fileRepository.findByAssetNo(commonAsset);


        // 유지보수이력을 가져오는 코드
        List<RepairHistory> repairHistory1 = repairHistoryRepository.findByAssetCode(commonAsset.getAssetCode());

        List<RepairHistoryDto> repairHistoryDtos = repairHistory1.stream()
                .map(repairHistory -> {
                    RepairHistoryDto repairHistoryDto = new RepairHistoryDto();
                    repairHistoryDto.setAssetNo(repairHistory.getAssetNo().getAssetNo());
                    repairHistoryDto.setRepairBy(repairHistory.getRepairBy());
                    repairHistoryDto.setRepairStartDate(repairHistory.getRepairStartDate());
                    repairHistoryDto.setRepairEnDate(repairHistory.getRepairEndDate());
                    repairHistoryDto.setRepairResult(repairHistory.getRepairResult());

                    // RepairFile 리스트를 가져와서 RepairFileDto 리스트로 변환
                    List<RepairFileDto> repairFileDtos = repairHistory.getRepairFiles().stream()
                            .map(RepairFile::toRepairFile) // RepairFile 객체를 RepairFileDto로 변환
                            .collect(Collectors.toList());

                    repairHistoryDto.setRepairFileDtos(repairFileDtos); // 리스트 설정
                    return repairHistoryDto;
                }).collect(Collectors.toList());

        // 자산조사 이력를 가져오는 코드
        List<AssetSurveyDetail> surveyDetailList = assetSurveyDetailRepository.findByAssetCode(commonAsset.getAssetCode());

        List<SurveyHistoryDto> surveyHistoryDtos = surveyDetailList.stream()
                .map(assetSurveyDetail -> {
                    SurveyHistoryDto surveyHistoryDto = new SurveyHistoryDto();
                    surveyHistoryDto.setAssetNo(assetSurveyDetail.getAssetNo().getAssetNo());
                    surveyHistoryDto.setAssetSurveyDetailNo(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyNo());
                    surveyHistoryDto.setAssetCode(assetSurveyDetail.getAssetNo().getAssetCode());
                    surveyHistoryDto.setAssetName(assetSurveyDetail.getAssetNo().getAssetName());
                    surveyHistoryDto.setRound(assetSurveyDetail.getAssetSurveyNo().getRound());
                    surveyHistoryDto.setAssetSurveyLocation(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyLocation());
                    surveyHistoryDto.setAssetSurveyStartDate(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyStartDate());
                    surveyHistoryDto.setAssetSurveyEndDate(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyEndDate());
                    surveyHistoryDto.setAssetSurveyBy(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyBy());
                    surveyHistoryDto.setExactLocation(assetSurveyDetail.getExactLocation());
                    surveyHistoryDto.setAssetStatus(assetSurveyDetail.getAssetStatus());
                    surveyHistoryDto.setAssetSurveyContent(assetSurveyDetail.getAssetSurveyContent());
                    return surveyHistoryDto;
                }).collect(Collectors.toList());


        // 수정이력을 가져오는 코드
        List<DemandDtl> updateHistory = demandDtlRepository.findUpdateHistoryByAssetCode(commonAsset.getAssetCode());

        //return assetDto;
        // 수정이력을 AssetDto에 추가
        List<UpdateHistoryDto> updateHistoryDtos = updateHistory.stream()
                .map(demandDtl -> {
                    UpdateHistoryDto updateHistoryDto = new UpdateHistoryDto();
                    updateHistoryDto.setAssetNo(demandDtl.getAssetNo().getAssetNo());
                    updateHistoryDto.setAssetCode(demandDtl.getAssetNo().getAssetCode());
                    updateHistoryDto.setAssetName(demandDtl.getAssetNo().getAssetName());
                    updateHistoryDto.setUpdateDate(demandDtl.getDemandNo().getDemandDate());
                    //updateHistoryDto.setUpdateBy(demandDtl.getDemandNo().getDemandBy());
                    updateHistoryDto.setUpdateReason(demandDtl.getDemandNo().getDemandReason());
                    updateHistoryDto.setUpdateDetail(demandDtl.getDemandNo().getDemandDetail());

                    return updateHistoryDto;
                }).collect(Collectors.toList());


        result.put("fileList", fileList != null ? fileList : Collections.emptyList());
        result.put("repairList", repairHistoryDtos);
        result.put("updateList", updateHistoryDtos);
        result.put("assetSurveyList", surveyHistoryDtos);
        result.put("assetDto", assetDto);
        return result;
    }

    //폐기 승인 처리
    public CommonAsset DisposeApprove(String assetCode) {

        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.TRUE);
        commonAssetRepository.save(commonAsset);

        return commonAsset;
    }

    // 폐기 거절 처리
    public CommonAsset DisposeRefusal(String assetCode, String Comment) {
        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
        commonAsset.setApproval(Approval.REFUSAL);
        commonAssetRepository.save(commonAsset);
        //코멘트 들어가야함

        return commonAsset;
    }

    //수정 승인 처리
    public CommonAsset UpdateApprove(String assetCode, String Comment) {
        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
        commonAsset.setApproval(Approval.APPROVE);
        commonAssetRepository.save(commonAsset);
        //코멘트 들어가야함
        return commonAsset;
    }

//    // 폐기 담당자 처리 (추후에 담당자랑 합칠거임)
//    public CommonAsset DisposeDemand(String assetCode, AssetDisposeDto assetDisposeDto) {
//        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
//        commonAsset.setApproval(Approval.UNCONFIRMED);
//        commonAsset.setDisposalStatus(Boolean.TRUE);
//        commonAsset.setDemandStatus(Boolean.TRUE);
//        commonAsset.setDemandCheck(Boolean.TRUE);
//        commonAssetRepository.save(commonAsset);
//
//        // 폐기 이력 저장
//        Demand demand = new Demand();
//        //demand.setDemandBy;
//        demand.setDemandDate(LocalDate.now()); // 폐기 일자 - 추후 자동생성 변경
//        demand.setDemandReason(assetDisposeDto.getDisposeReason()); // 폐기 사유
//        demand.setDemandDetail(assetDisposeDto.getDisposeDetail()); // 폐기내용
//        demand.setDisposeMethod(assetDisposeDto.getDisposeMethod()); // 폐기 방법
//        demand.setDisposeLocation(assetDisposeDto.getDisposeLocation());  // 폐기 위치
//        demandRepository.save(demand);
//
//        // DemandDtl 테이블에 저장
//        DemandDtl demandDtl = new DemandDtl();
//        demandDtl.setAssetNo(commonAsset);  // CommonAsset과 연관 설정
//        demandDtl.setDemandNo(demand);  // Demand와 연관 설정
//        demandDtlRepository.save(demandDtl); // DemandDtl 테이블에 저장
//
//        return commonAsset;
//    }

    // 수정 이력 상세화면 자산 2개 비교하기 (요청버전)
    public List<AssetDto> getLatestAndPreviousAssets1(Long assetNo) {

        // 자산코드 (assetCode)로  최신 자산(수정) 과 그 이전 자산 가져오기
        List<CommonAsset> assets = commonAssetRepository.findNextAssetsByAssetNo1(assetNo);
        System.out.println("ssss"+assets.size());
        if (assets.size() < 2) {
            throw new RuntimeException("이전 자산 정보를 찾을 수 없습니다.");
        }
        // 이미 존재하는 AssetUpdateDto를 그대로 사용하여 리스트로 반환
        List<AssetDto> assetDtos = new ArrayList<>();
        for (CommonAsset asset : assets) {

            String assetOwnerFullname = userService.getUserById(asset.getAssetOwner()) != null
                    ? userService.getUserById(asset.getAssetOwner()).getFullname()
                    : "Unknown Owner";

            String assetUserFullname = userService.getUserById(asset.getAssetUser()) != null
                    ? userService.getUserById(asset.getAssetUser()).getFullname()
                    : "Unknown User";

            String assetSecurityManagerFullname = userService.getUserById(asset.getAssetSecurityManager()) != null
                    ? userService.getUserById(asset.getAssetSecurityManager()).getFullname()
                    : "Unknown Security Manager";

            AssetDto dto = AssetDto.builder()
                    .assetNo(asset.getAssetNo())
                    .assetClassification(asset.getAssetClassification())
                    .assetBasis(asset.getAssetBasis())
                    .assetCode(asset.getAssetCode())
                    .assetName(asset.getAssetName())
                    .purpose(asset.getPurpose())
                    .quantity(asset.getQuantity())
                    .department(asset.getDepartment())
                    .assetLocation(asset.getAssetLocation())
                    .operationStatus(asset.getOperationStatus())
                    .introducedDate(asset.getIntroducedDate())
                    .confidentiality(asset.getConfidentiality())
                    .integrity(asset.getIntegrity())
                    .availability(asset.getAvailability())
                    .note(asset.getNote())
                    .manufacturingCompany(asset.getManufacturingCompany())
                    .ownership(asset.getOwnership())
                    .productSerialNumber(asset.getProductSerialNumber())
                    .purchaseCost(asset.getPurchaseCost())
                    .purchaseDate(asset.getPurchaseDate())
                    .usefulLife(asset.getUsefulLife())
                    .depreciationMethod(asset.getDepreciationMethod())
                    .purchaseSource(asset.getPurchaseSource())
                    .contactInformation(asset.getContactInformation())
                    .useStated(asset.getUseStated())
                    .acquisitionRoute(asset.getAcquisitionRoute())
                    .maintenancePeriod(asset.getMaintenancePeriod())
                    .assetOwner(assetOwnerFullname)
                    .assetUser(assetUserFullname)
                    .assetSecurityManager(assetSecurityManagerFullname)
                    .build(); // 공통필드 builder 하고

            // classification에 따라 추가 엔티티 정보를 조회
            switch (asset.getAssetClassification()) {
                case INFORMATION_PROTECTION_SYSTEM -> {
                    InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(asset);
                    if (informationProtectionSystem != null) {
                        dto.setServiceScope(informationProtectionSystem.getServiceScope());
                    }
                }
                case APPLICATION_PROGRAM -> {
                    ApplicationProgram applicationProgram = applicationProgramRepository.findByAssetNo(asset);
                    if (applicationProgram != null) {
                        dto.setServiceScope(applicationProgram.getServiceScope());
                        dto.setOs(applicationProgram.getOs());
                        dto.setRelatedDB(applicationProgram.getRelatedDB());
                        dto.setIp(applicationProgram.getIp());
                        dto.setScreenNumber(applicationProgram.getScreenNumber());
                    }
                }
                case SOFTWARE -> {
                    Software software = softwareRepository.findByAssetNo(asset);
                    if (software != null) {
                        dto.setIp(software.getIp());
                        dto.setServerId(software.getServerId());
                        dto.setServerPassword(software.getServerPassword());
                        dto.setCompanyManager(software.getCompanyManager());
                        dto.setOs(software.getOs());
                    }
                }
                case ELECTRONIC_INFORMATION -> {
                    ElectronicInformation electronicInformation = electronicInformationRepository.findByAssetNo(asset);
                    if (electronicInformation != null) {
                        dto.setOs(electronicInformation.getOs());
                        dto.setSystem(electronicInformation.getSystem());
                        dto.setDbtype(electronicInformation.getDbtype());
                    }
                }
                case DOCUMENT -> {
                    Document document = documentRepository.findByAssetNo(asset);
                    if (document != null) {
                        dto.setDocumentGrade(document.getDocumentGrade());
                        dto.setDocumentType(document.getDocumentType());
                        dto.setDocumentLink(document.getDocumentLink());
                    }
                }
                case PATENTS_AND_TRADEMARKS -> {
                    PatentsAndTrademarks patentsAndTrademarks = patentsAndTrademarksRepository.findByAssetNo(asset);
                    if (patentsAndTrademarks != null) {
                        dto.setApplicationDate(patentsAndTrademarks.getApplicationDate());
                        dto.setRegistrationDate(patentsAndTrademarks.getRegistrationDate());
                        dto.setExpirationDate(patentsAndTrademarks.getExpirationDate());
                        dto.setPatentTrademarkStatus(patentsAndTrademarks.getPatentTrademarkStatus());
                        dto.setCountryApplication(patentsAndTrademarks.getCountryApplication());
                        dto.setPatentClassification(patentsAndTrademarks.getPatentClassification());
                        dto.setPatentItem(patentsAndTrademarks.getPatentItem());
                        dto.setApplicationNo(patentsAndTrademarks.getApplicationNo());
                        dto.setInventor(patentsAndTrademarks.getInventor());
                        dto.setAssignee(patentsAndTrademarks.getAssignee());
                    }
                }
                case ITSYSTEM_EQUIPMENT -> {
                    ItSystemEquipment itSystemEquipment = itSystemEquipmentRepository.findByAssetNo(asset);
                    if (itSystemEquipment != null) {
                        dto.setEquipmentType(itSystemEquipment.getEquipmentType());
                        dto.setPowerSupply(itSystemEquipment.getPowerSupply());
                        dto.setCoolingSystem(itSystemEquipment.getCoolingSystem());
                        dto.setInterfacePorts(itSystemEquipment.getInterfacePorts());
                        dto.setFormFactor(itSystemEquipment.getFormFactor());
                        dto.setExpansionSlots(itSystemEquipment.getExpansionSlots());
                        dto.setGraphicsCard(itSystemEquipment.getGraphicsCard());
                        dto.setPortConfiguration(itSystemEquipment.getPortConfiguration());
                        dto.setMonitorIncluded(itSystemEquipment.getMonitorIncluded());
                    }
                }
                case ITNETWORK_EQUIPMENT -> {
                    ItNetworkEquipment itNetworkEquipment = itNetworkEquipmentRepository.findByAssetNo(asset);
                    if (itNetworkEquipment != null) {
                        dto.setEquipmentType(itNetworkEquipment.getEquipmentType());
                        dto.setNumberOfPorts(itNetworkEquipment.getNumberOfPorts());
                        dto.setSupportedProtocols(itNetworkEquipment.getSupportedProtocols());
                        dto.setFirmwareVersion(itNetworkEquipment.getFirmwareVersion());
                        dto.setNetworkSpeed(itNetworkEquipment.getNetworkSpeed());
                        dto.setServiceScope(itNetworkEquipment.getServiceScope());
                    }
                }
                case TERMINAL -> {
                    Terminal terminal = terminalRepository.findByAssetNo(asset);
                    if (terminal != null) {
                        dto.setIp(terminal.getIp());
                        dto.setOs(terminal.getOs());
                        dto.setSecurityControl(terminal.getSecurityControl());
                        dto.setKaitsKeeper(terminal.getKaitsKeeper());
                        dto.setV3OfficeSecurity(terminal.getV3OfficeSecurity());
                        dto.setAppCheckPro(terminal.getAppCheckPro());
                        dto.setTgate(terminal.getTgate());
                    }
                }
                case FURNITURE -> {
                    Furniture furniture = furnitureRepository.findByAssetNo(asset);
                    if (furniture != null) {
                        dto.setFurnitureSize(furniture.getFurnitureSize());
                    }
                }
                case DEVICES -> {
                    Devices devices = devicesRepository.findByAssetNo(asset);
                    if (devices != null) {
                        dto.setDeviceType(devices.getDeviceType());
                        dto.setModelNumber(devices.getModelNumber());
                        dto.setConnectionType(devices.getConnectionType());
                        dto.setPowerSpecifications(devices.getPowerSpecifications());
                    }
                }
                case CAR -> {
                    Car car = carRepository.findByAssetNo(asset);
                    if (car != null) {
                        dto.setDisplacement(car.getDisplacement());
                        dto.setDoorsCount(car.getDoorsCount());
                        dto.setEngineType(car.getEngineType());
                        dto.setCarType(car.getCarType());
                        dto.setIdentificationNo(car.getIdentificationNo());
                        dto.setCarColor(car.getCarColor());
                        dto.setModelYear(car.getModelYear());
                    }
                }
                case OTHERASSETS -> {
                    OtherAssets otherAssets = otherAssetsRepository.findByAssetNo(asset);
                    if (otherAssets != null) {
                        dto.setOtherDescription(otherAssets.getOtherDescription());
                        dto.setUsageFrequency(otherAssets.getUsageFrequency());
                    }
                }
            }
            assetDtos.add(dto);
        }
        return assetDtos;
    }

    // 수정 이력 상세화면 자산 2개 비교하기
    public List<AssetDto> getUpdateDetail(Long assetNo) {

        List<CommonAsset> assets = new ArrayList<>();
        // 자산코드 (assetCode)로  최신 자산(수정) 과 그 이전 자산 가져오기
        CommonAsset beforeAsset = commonAssetRepository.findNextAssetByAssetNo(assetNo);
        CommonAsset afterAsset = commonAssetRepository.findById(assetNo).orElseThrow();
        assets.add(beforeAsset);
        assets.add(afterAsset);

        // 이미 존재하는 AssetUpdateDto를 그대로 사용하여 리스트로 반환
        List<AssetDto> assetDtos = new ArrayList<>();
        for (CommonAsset asset : assets) {
            AssetDto dto = AssetDto.builder()
                    .assetNo(asset.getAssetNo())
                    .assetClassification(asset.getAssetClassification())
                    .assetBasis(asset.getAssetBasis())
                    .assetCode(asset.getAssetCode())
                    .assetName(asset.getAssetName())
                    .purpose(asset.getPurpose())
                    .quantity(asset.getQuantity())
                    .department(asset.getDepartment())
                    .assetLocation(asset.getAssetLocation())
                    .operationStatus(asset.getOperationStatus())
                    .introducedDate(asset.getIntroducedDate())
                    .confidentiality(asset.getConfidentiality())
                    .integrity(asset.getIntegrity())
                    .availability(asset.getAvailability())
                    .note(asset.getNote())
                    .manufacturingCompany(asset.getManufacturingCompany())
                    .ownership(asset.getOwnership())
                    .purchaseCost(asset.getPurchaseCost())
                    .purchaseDate(asset.getPurchaseDate())
                    .usefulLife(asset.getUsefulLife())
                    .depreciationMethod(asset.getDepreciationMethod())
                    .purchaseSource(asset.getPurchaseSource())
                    .contactInformation(asset.getContactInformation())
                    .useStated(asset.getUseStated())
                    .acquisitionRoute(asset.getAcquisitionRoute())
                    .maintenancePeriod(asset.getMaintenancePeriod())
                    .build(); // 공통필드 builder 하고

            // classification에 따라 추가 엔티티 정보를 조회
            switch (asset.getAssetClassification()) {
                case INFORMATION_PROTECTION_SYSTEM -> {
                    InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(asset);
                    if (informationProtectionSystem != null) {
                        dto.setServiceScope(informationProtectionSystem.getServiceScope());
                    }
                }
                case APPLICATION_PROGRAM -> {
                    ApplicationProgram applicationProgram = applicationProgramRepository.findByAssetNo(asset);
                    if (applicationProgram != null) {
                        dto.setServiceScope(applicationProgram.getServiceScope());
                        dto.setOs(applicationProgram.getOs());
                        dto.setRelatedDB(applicationProgram.getRelatedDB());
                        dto.setIp(applicationProgram.getIp());
                        dto.setScreenNumber(applicationProgram.getScreenNumber());
                    }
                }
                case SOFTWARE -> {
                    Software software = softwareRepository.findByAssetNo(asset);
                    if (software != null) {
                        dto.setIp(software.getIp());
                        dto.setServerId(software.getServerId());
                        dto.setServerPassword(software.getServerPassword());
                        dto.setCompanyManager(software.getCompanyManager());
                        dto.setOs(software.getOs());
                    }
                }
                case ELECTRONIC_INFORMATION -> {
                    ElectronicInformation electronicInformation = electronicInformationRepository.findByAssetNo(asset);
                    if (electronicInformation != null) {
                        dto.setOs(electronicInformation.getOs());
                        dto.setSystem(electronicInformation.getSystem());
                        dto.setDbtype(electronicInformation.getDbtype());
                    }
                }
                case DOCUMENT -> {
                    Document document = documentRepository.findByAssetNo(asset);
                    if (document != null) {
                        dto.setDocumentGrade(document.getDocumentGrade());
                        dto.setDocumentType(document.getDocumentType());
                        dto.setDocumentLink(document.getDocumentLink());
                    }
                }
                case PATENTS_AND_TRADEMARKS -> {
                    PatentsAndTrademarks patentsAndTrademarks = patentsAndTrademarksRepository.findByAssetNo(asset);
                    if (patentsAndTrademarks != null) {
                        dto.setApplicationDate(patentsAndTrademarks.getApplicationDate());
                        dto.setRegistrationDate(patentsAndTrademarks.getRegistrationDate());
                        dto.setExpirationDate(patentsAndTrademarks.getExpirationDate());
                        dto.setPatentTrademarkStatus(patentsAndTrademarks.getPatentTrademarkStatus());
                        dto.setCountryApplication(patentsAndTrademarks.getCountryApplication());
                        dto.setPatentClassification(patentsAndTrademarks.getPatentClassification());
                        dto.setPatentItem(patentsAndTrademarks.getPatentItem());
                        dto.setApplicationNo(patentsAndTrademarks.getApplicationNo());
                        dto.setInventor(patentsAndTrademarks.getInventor());
                        dto.setAssignee(patentsAndTrademarks.getAssignee());
                    }
                }
                case ITSYSTEM_EQUIPMENT -> {
                    ItSystemEquipment itSystemEquipment = itSystemEquipmentRepository.findByAssetNo(asset);
                    if (itSystemEquipment != null) {
                        dto.setEquipmentType(itSystemEquipment.getEquipmentType());
                        dto.setPowerSupply(itSystemEquipment.getPowerSupply());
                        dto.setCoolingSystem(itSystemEquipment.getCoolingSystem());
                        dto.setInterfacePorts(itSystemEquipment.getInterfacePorts());
                        dto.setFormFactor(itSystemEquipment.getFormFactor());
                        dto.setExpansionSlots(itSystemEquipment.getExpansionSlots());
                        dto.setGraphicsCard(itSystemEquipment.getGraphicsCard());
                        dto.setPortConfiguration(itSystemEquipment.getPortConfiguration());
                        dto.setMonitorIncluded(itSystemEquipment.getMonitorIncluded());
                    }
                }
                case ITNETWORK_EQUIPMENT -> {
                    ItNetworkEquipment itNetworkEquipment = itNetworkEquipmentRepository.findByAssetNo(asset);
                    if (itNetworkEquipment != null) {
                        dto.setEquipmentType(itNetworkEquipment.getEquipmentType());
                        dto.setNumberOfPorts(itNetworkEquipment.getNumberOfPorts());
                        dto.setSupportedProtocols(itNetworkEquipment.getSupportedProtocols());
                        dto.setFirmwareVersion(itNetworkEquipment.getFirmwareVersion());
                        dto.setNetworkSpeed(itNetworkEquipment.getNetworkSpeed());
                        dto.setServiceScope(itNetworkEquipment.getServiceScope());
                    }
                }
                case TERMINAL -> {
                    Terminal terminal = terminalRepository.findByAssetNo(asset);
                    if (terminal != null) {
                        dto.setIp(terminal.getIp());
                        dto.setOs(terminal.getOs());
                        dto.setSecurityControl(terminal.getSecurityControl());
                        dto.setKaitsKeeper(terminal.getKaitsKeeper());
                        dto.setV3OfficeSecurity(terminal.getV3OfficeSecurity());
                        dto.setAppCheckPro(terminal.getAppCheckPro());
                        dto.setTgate(terminal.getTgate());
                    }
                }
                case FURNITURE -> {
                    Furniture furniture = furnitureRepository.findByAssetNo(asset);
                    if (furniture != null) {
                        dto.setFurnitureSize(furniture.getFurnitureSize());
                    }
                }
                case DEVICES -> {
                    Devices devices = devicesRepository.findByAssetNo(asset);
                    if (devices != null) {
                        dto.setDeviceType(devices.getDeviceType());
                        dto.setModelNumber(devices.getModelNumber());
                        dto.setConnectionType(devices.getConnectionType());
                        dto.setPowerSpecifications(devices.getPowerSpecifications());
                    }
                }
                case CAR -> {
                    Car car = carRepository.findByAssetNo(asset);
                    if (car != null) {
                        dto.setDisplacement(car.getDisplacement());
                        dto.setDoorsCount(car.getDoorsCount());
                        dto.setEngineType(car.getEngineType());
                        dto.setCarType(car.getCarType());
                        dto.setIdentificationNo(car.getIdentificationNo());
                        dto.setCarColor(car.getCarColor());
                        dto.setModelYear(car.getModelYear());
                    }
                }
                case OTHERASSETS -> {
                    OtherAssets otherAssets = otherAssetsRepository.findByAssetNo(asset);
                    if (otherAssets != null) {
                        dto.setOtherDescription(otherAssets.getOtherDescription());
                        dto.setUsageFrequency(otherAssets.getUsageFrequency());
                    }
                }
            }
            assetDtos.add(dto);
        }
        return assetDtos;
    }

    public Approval demandCheck(String assetCode){

        commonAssetRepository.findAssetApprovalByAssetCode(assetCode);

        return commonAssetRepository.findAssetApprovalByAssetCode(assetCode);
    }

}