package com.codehows.taelim.service;


import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileOutputStream;
import java.io.IOException;
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
            AssetClassification assetClassification,  // 추가된 assetClassification 파라미터
            int page,
            int size) {

        // Pageable 객체 설정 처리
        Pageable pageable = PageRequest.of(page, size);

        Page<CommonAsset> assetPage;
        // assetClassification이 null인지 체크
        if (assetClassification == null) {
            // 전체 자산 조회 로직
            assetPage = commonAssetRepository.findApprovedAndNotDisposedAssetsWithSearch(
                    assetName,
                    assetLocationString,
                    assetLocationEnum,
                    assetUser,
                    departmentString,
                    departmentEnum,
                    introducedDate,
                    null, // assetClassification을 null로 설정
                    pageable
            );
        } else {
            // 분류별 자산 조회 로직
            assetPage = commonAssetRepository.findApprovedAndNotDisposedAssetsWithSearch(
                    assetName,
                    assetLocationString,
                    assetLocationEnum,
                    assetUser,
                    departmentString,
                    departmentEnum,
                    introducedDate,
                    assetClassification, // 분류 정보 전달
                    pageable
            );
        }

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

            if (commonAsset.getAssetClassification() != null) {
                switch (commonAsset.getAssetClassification()) {
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

//    public void exportAssetsToExcel(String assetClassification, HttpServletResponse response) throws IOException {
//        List<CommonAsset> assets = findAssetByExcel(assetClassification); // 자산 목록 조회 메소드
//        Workbook workbook = new XSSFWorkbook();
//
//        // 분류별로 시트 생성
//        for (AssetClassification classification : AssetClassification.values()) {
//            Sheet sheet = workbook.createSheet(classification.getDescription());
//
//            // 헤더 생성
//            Row headerRow = sheet.createRow(8);
//            createHeaderCell(headerRow,0,"No");
//            createHeaderCell(headerRow, 1, "자산 기준");
//            createHeaderCell(headerRow, 2, "자산 코드");
//            createHeaderCell(headerRow,3,"자산명");
//            createHeaderCell(headerRow,4,"자산분류");
//            createHeaderCell(headerRow,5,"목적/기능");
//            createHeaderCell(headerRow, 6 ,"자산 위치");
//            createHeaderCell(headerRow,7,"부서");
//            createHeaderCell(headerRow,8,"사용자");
//            createHeaderCell(headerRow,9, "소유자");
//            createHeaderCell(headerRow,10, "보안담당자");
//            createHeaderCell(headerRow,11, "사용상태");
//            createHeaderCell(headerRow,12, "가동여부");
//            createHeaderCell(headerRow,13, "기밀성");
//            createHeaderCell(headerRow,14, "무결성");
//            createHeaderCell(headerRow,15, "가용성");
//            createHeaderCell(headerRow,16, "중요성점수");
//            createHeaderCell(headerRow,17, "중요성등급");
//            createHeaderCell(headerRow,18, "비고");
//
//
//            // 추가적인 헤더 셀 생성...
//            int rowIndex = 1;
//            for (CommonAsset asset : assets) {
//                if (asset.getAssetClassification() == classification) {
//                    Row row = sheet.createRow(rowIndex++);
//                    row.createCell(0).setCellValue(asset.getAssetNo());
//                    row.createCell(1).setCellValue(asset.getAssetBasis().toString());
//                    row.createCell(2).setCellValue(asset.getAssetCode());
//                    row.createCell(3).setCellValue(asset.getAssetName());
//                    row.createCell(4).setCellValue(asset.getAssetClassification().getDescription());
//
//                    // 자산 분류에 따라 추가적인 정보 처리
//                    switch (classification) {
//                        case SOFTWARE -> {
//                            Software software = softwareRepository.findByAssetNo(asset);
//                            row.createCell(5).setCellValue(software.getIp());
//                            row.createCell(6).setCellValue(software.getOs());
//                            // 기타 소프트웨어 관련 정보 추가
//                        }
//                        case CAR -> {
//                            Car car = carRepository.findByAssetNo(asset);
//                            row.createCell(5).setCellValue(car.getDisplacement());
//                            row.createCell(6).setCellValue(car.getDoorsCount());
//                            // 기타 차량 관련 정보 추가
//                        }
//                        case DEVICES -> {
//                            Devices devices = devicesRepository.findByAssetNo(asset);
//                            row.createCell(5).setCellValue(devices.getDeviceType());
//                            row.createCell(6).setCellValue(devices.getModelNumber());
//                            // 기타 장비 관련 정보 추가
//                        }
//                        // 나머지 분류별 처리 추가...
//                    }
//                }
//            }
//        }
//
//
//        // 응답에 엑셀 파일 작성
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=assets.xlsx");
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }
//
//    private void createHeaderCell(Row row, int column, String value) {
//        Cell cell = row.createCell(column);
//        cell.setCellValue(value);
//
//        Workbook workbook = row.getSheet().getWorkbook();
//        Font font = workbook.createFont();
//        font.setBold(true);
//
//        CellStyle style = workbook.createCellStyle();
//        style.setFont(font);
//
//        cell.setCellStyle(style);
//    }

    public List<CommonAsset> findAssetByExcel(AssetClassification assetClassification) {
        // 자산 분류가 null인지 확인하고 기본값 또는 예외 처리 추가
        if (assetClassification == null) {
            // assetClassification이 null일 경우 전체 자산 조회
            return commonAssetRepository.findAssetByExcel(null); // null을 넘겨서 전체 조회를 처리하도록
        } else {
            // assetClassification이 있을 경우 해당 분류에 따라 자산 목록을 조회
            return commonAssetRepository.findAssetByExcel(assetClassification);
        }

    }

}
