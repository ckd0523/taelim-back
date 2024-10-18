package com.codehows.taelim.service;


import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AssetFinalService {

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



    // 자산 상세 조회 - 이걸로 리스트 검색쿼리 + pageNation 까지 해보기
    public PaginatedResponse<AssetDto> getAssetSearch(
            String assetName,
            String assetLocationString,
            AssetLocation assetLocationEnum,
            String assetUser,
            String departmentString,
            Department departmentEnum,
            LocalDate introducedDate,
            int page,
            int size) {

        // Pageable 객체 설정 처리
        Pageable pageable = PageRequest.of(page, size);

        // 검색조건에 따라 페이지네이션된 자산 리스트 조회
        Page<CommonAsset> assetPage = commonAssetRepository.findApprovedAndNotDisposedAssetsWithSearch(
                assetName,
                assetLocationString,
                assetLocationEnum,
                assetUser,
                departmentString,
                departmentEnum,
                introducedDate,
                pageable
        );

        // AssetDto 목록 생성
        List<AssetDto> assetDtos = new ArrayList<>();
        for (CommonAsset commonAsset : assetPage.getContent()) {

            AssetDto assetDto = new AssetDto();
            assetDto.setAssetNo(commonAsset.getAssetNo());
            assetDto.setAssetBasis(commonAsset.getAssetBasis());
            assetDto.setAssetCode(commonAsset.getAssetCode());
            assetDto.setAssetClassification(commonAsset.getAssetClassification());
            assetDto.setAssetName(commonAsset.getAssetName());
            assetDto.setManufacturingCompany(commonAsset.getManufacturingCompany());
            assetDto.setPurpose(commonAsset.getPurpose());
            assetDto.setDepartment(commonAsset.getDepartment());
            assetDto.setAssetLocation(commonAsset.getAssetLocation());

            // assetUser, assetOwner, assetSecurityManager가 null일 경우 처리
            assetDto.setAssetUser(commonAsset.getAssetUser() != null ? commonAsset.getAssetUser().getUName() : "Unknown User");
            assetDto.setAssetOwner(commonAsset.getAssetOwner() != null ? commonAsset.getAssetOwner().getUName() : "Unknown Owner");
            assetDto.setAssetSecurityManager(commonAsset.getAssetSecurityManager() != null ? commonAsset.getAssetSecurityManager().getUName() : "Unknown Manager");

            assetDto.setUsestate(commonAsset.getUseState());
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
                    assetDto.setCompanyManager(software.getCompanyManager());
                    assetDto.setIp(software.getIp());
                    assetDto.setOs(software.getOs());
                    assetDto.setServerId(software.getServerId());
                    assetDto.setServerPassword(software.getServerPassword());
                }
                case CAR -> {
                    Car car = carRepository.findByAssetNo(commonAsset);
                    assetDto.setDisplacement(car.getDisplacement());
                    assetDto.setDoorsCount(car.getDoorsCount());
                    assetDto.setEngineType(car.getEngineType());
                    assetDto.setCarType(car.getCarType());
                    assetDto.setIdentificationNo(car.getIdentificationNo());
                    assetDto.setCarColor(car.getCarColor());
                    assetDto.setModelYear(car.getModelYear());
                }
                case DEVICES -> {

                    Devices devices = devicesRepository.findByAssetNo(commonAsset);
                    assetDto.setDeviceType(devices.getDeviceType());
                    assetDto.setModelNumber(devices.getModelNumber());
                    assetDto.setConnectionType(devices.getConnectionType());
                    assetDto.setPowerSpecifications(devices.getPowerSpecifications());

                }
                case DOCUMENT -> {

                    Document document = documentRepository.findByAssetNo(commonAsset);
                    assetDto.setDocumentGrade(document.getDocumentGrade());
                    assetDto.setDocumentType(document.getDocumentType());
                    assetDto.setDocumentLink(document.getDocumentLink());
                }
                case TERMINAL -> {
                    Terminal terminal = terminalRepository.findByAssetNo(commonAsset);
                    assetDto.setIp(terminal.getIp());
                    //assetDto.setProductSerialNumber(terminal.getProductSerialNumber());
                    assetDto.setOs(terminal.getOs());
                    assetDto.setSecurityControl(terminal.getSecurityControl());
                    assetDto.setKaitsKeeper(terminal.getKaitsKeeper());
                    assetDto.setV3OfficeSecurity(terminal.getV3OfficeSecurity());
                    assetDto.setAppCheckPro(terminal.getAppCheckPro());
                    assetDto.setTgate(terminal.getTgate());

                }
                case FURNITURE -> {
                    Furniture furniture = furnitureRepository.findByAssetNo(commonAsset);
                    assetDto.setFurnitureSize(furniture.getFurnitureSize());

                }
                case OTHERASSETS -> {
                    OtherAssets otherAssets = otherAssetsRepository.findByAssetNo(commonAsset);
                    assetDto.setOtherDescription(otherAssets.getOtherDescription());
                    assetDto.setUsageFrequency(otherAssets.getUsageFrequency());
                }
                case ITSYSTEM_EQUIPMENT -> {
                    ItSystemEquipment itSystemEquipment = itSystemEquipmentRepository.findByAssetNo(commonAsset);
                    assetDto.setEquipmentType(itSystemEquipment.getEquipmentType());
                    //assetDto.setRackUnit(itSystemEquipment.getRackUnit());
                    assetDto.setPowerSupply(itSystemEquipment.getPowerSupply());
                    assetDto.setCoolingSystem(itSystemEquipment.getCoolingSystem());
                    assetDto.setInterfacePorts(itSystemEquipment.getInterfacePorts());
                    assetDto.setFormFactor(itSystemEquipment.getFormFactor());
                    assetDto.setExpansionSlots(itSystemEquipment.getExpansionSlots());
                    assetDto.setGraphicsCard(itSystemEquipment.getGraphicsCard());
                    assetDto.setPortConfiguration(itSystemEquipment.getPortConfiguration());
                    assetDto.setMonitorIncluded(itSystemEquipment.getMonitorIncluded());

                }
                case APPLICATION_PROGRAM -> {
                    ApplicationProgram applicationProgram = applicationProgramRepository.findByAssetNo(commonAsset);
                    assetDto.setServiceScope(applicationProgram.getServiceScope());
                    assetDto.setOs(applicationProgram.getOs());
                    assetDto.setRelatedDB(applicationProgram.getRelatedDB());
                    assetDto.setIp(applicationProgram.getIp());
                    assetDto.setScreenNumber(applicationProgram.getScreenNumber());
                }
                case ITNETWORK_EQUIPMENT -> {
                    ItNetworkEquipment itNetworkEquipment = itNetworkEquipmentRepository.findByAssetNo(commonAsset);
                    assetDto.setEquipmentType(itNetworkEquipment.getEquipmentType());
                    assetDto.setNumberOfPorts(itNetworkEquipment.getNumberOfPorts());
                    assetDto.setSupportedProtocols(itNetworkEquipment.getSupportedProtocols());
                    assetDto.setFirmwareVersion(itNetworkEquipment.getFirmwareVersion());
                    assetDto.setNetworkSpeed(itNetworkEquipment.getNetworkSpeed());
                    assetDto.setServiceScope(itNetworkEquipment.getServiceScope());
                }
                case ELECTRONIC_INFORMATION -> {
                    ElectronicInformation electronicInformation = electronicInformationRepository.findByAssetNo(commonAsset);
                    assetDto.setOs(electronicInformation.getOs());
                    assetDto.setSystem(electronicInformation.getSystem());
                    assetDto.setDbtype(electronicInformation.getDbtype());
                }
                case PATENTS_AND_TRADEMARKS -> {
                    PatentsAndTrademarks patentsAndTrademarks = patentsAndTrademarksRepository.findByAssetNo(commonAsset);
                    assetDto.setApplicationDate(patentsAndTrademarks.getApplicationDate());
                    assetDto.setRegistrationDate(patentsAndTrademarks.getRegistrationDate());
                    assetDto.setExpirationDate(patentsAndTrademarks.getExpirationDate());
                    assetDto.setPatentTrademarkStatus(patentsAndTrademarks.getPatentTrademarkStatus());
                    assetDto.setCountryApplication(patentsAndTrademarks.getCountryApplication());
                    assetDto.setPatentClassification(patentsAndTrademarks.getPatentClassification());
                    assetDto.setPatentItem(patentsAndTrademarks.getPatentItem());
                    assetDto.setApplicationNo(patentsAndTrademarks.getApplicationNo());
                    assetDto.setInventor(patentsAndTrademarks.getInventor());
                    assetDto.setAssignee(patentsAndTrademarks.getAssignee());
                    //assetDto.setRelatedDocuments(patentsAndTrademarks.getRelatedDocuments());
                }
                case INFORMATION_PROTECTION_SYSTEM -> {
                    InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(commonAsset);
                    assetDto.setServiceScope(informationProtectionSystem.getServiceScope());
                }

            }

            List<File> files = fileRepository.findByAssetNo(commonAsset);

            List<FileDto> fileDtos = files.stream()
                    .map(file -> {
                        FileDto fileDto = new FileDto();
                        fileDto.setAssetNo(file.getAssetNo().getAssetNo());
                        fileDto.setFileNo(file.getFileNo());
                        fileDto.setOriFileName(file.getOriFileName());
                        fileDto.setFileName(file.getFileName());
                        fileDto.setFileSize(file.getFileSize());
                        fileDto.setFileURL(file.getFileURL());
                        fileDto.setFileExt(file.getFileExt());
                        fileDto.setFileType(file.getFileType());
                        return fileDto;
                    }).collect(Collectors.toList());


            assetDto.setFiles(fileDtos);

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
            assetDto.setUpdateHistory(updateHistoryDtos);

            // 유지보수이력을 가져오는 코드
            List<RepairHistory> repairHistory1 = repairHistoryRepository.findByAssetCode(commonAsset.getAssetCode());

            List<RepairHistoryDto> repairHistoryDtos = repairHistory1.stream()
                    .map(repairHistory -> {
                        RepairHistoryDto repairHistoryDto = new RepairHistoryDto();
                        repairHistoryDto.setAssetNo(repairHistory.getAssetNo().getAssetNo());
                        repairHistoryDto.setAssetCode(repairHistory.getAssetNo().getAssetCode());
                        repairHistoryDto.setAssetName(repairHistory.getAssetNo().getAssetName());
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

            assetDto.setRepairHistory(repairHistoryDtos);

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
                        surveyHistoryDto.setAssetSurveyBy(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyBy().getUName());
                        surveyHistoryDto.setExactLocation(assetSurveyDetail.getExactLocation());
                        surveyHistoryDto.setAssetStatus(assetSurveyDetail.getAssetStatus());
                        surveyHistoryDto.setAssetSurveyContent(assetSurveyDetail.getAssetSurveyContent());

                        return surveyHistoryDto;
                    }).collect(Collectors.toList());
            assetDto.setSurveyHistory(surveyHistoryDtos);

            assetDtos.add(assetDto);
        }
        // PaginatedResponse 생성

        return new PaginatedResponse<AssetDto>( // AssetDto 타입 명시
                assetDtos,                      // List<AssetDto>
                assetPage.getNumber(),          // 현재 페이지 번호
                assetPage.getSize(),            // 페이지 사이즈
                assetPage.getTotalElements(),    // 총 요소 수
                assetPage.getTotalPages()       // 총 페이지 수
        );
    }
}
