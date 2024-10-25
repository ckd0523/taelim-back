package com.codehows.taelim.service;


import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.constant.Department;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final UserService userService;


    // 자산 상세 조회 - 이걸로 리스트 검색쿼리 + pageNation 까지 해보기
    public PaginatedResponse<AssetDto> getAssetSearch(
            String assetName,
            AssetLocation assetLocationEnum,
            String assetUser,
            Department departmentEnum,
            LocalDate startDate, // 검색 범위 시작 날짜
            LocalDate endDate,   // 검색 범위 종료 날짜
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
                    assetLocationEnum, // assetLocationString 제거
                    assetUser,
                    departmentEnum,
                    startDate, // 검색 범위 시작 날짜
                    endDate,   // 검색 범위 종료 날짜
                    null, // assetClassification을 null로 설정
                    pageable
            );
        } else {
            // 분류별 자산 조회 로직
            assetPage = commonAssetRepository.findApprovedAndNotDisposedAssetsWithSearch(
                    assetName,
                    assetLocationEnum, // assetLocationString 제거
                    assetUser,
                    departmentEnum,
                    startDate, // 검색 범위 시작 날짜
                    endDate,   // 검색 범위 종료 날짜
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
            UserDto userAsset  = userService.getUserById(commonAsset.getAssetUser());
            assetDto.setAssetUser(userAsset  != null ? userAsset.getFullname() : "Unknown User");

            UserDto assetOwner = userService.getUserById(commonAsset.getAssetOwner());
            assetDto.setAssetOwner(assetOwner != null ? assetOwner.getFullname() : "Unknown Owner");

            UserDto assetSecurityManager = userService.getUserById(commonAsset.getAssetSecurityManager());
            assetDto.setAssetSecurityManager(assetSecurityManager != null ? assetSecurityManager.getFullname() : "Unknown Manager");
            assetDto.setAssetUserId(commonAsset.getAssetUser());  // ID 값 저장
            assetDto.setAssetOwnerId(commonAsset.getAssetOwner());  // ID 값 저장
            assetDto.setAssetSecurityManagerId(commonAsset.getAssetSecurityManager());  // ID 값 저장

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
                        updateHistoryDto.setUpdateBy(userService.getUserById(demandDtl.getDemandNo().getDemandBy()).getFullname());
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
                        //repairHistoryDto.setRepairBy(userService.getUserById(repairHistory.getRepairBy()).getFullname());
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
                        surveyHistoryDto.setAssetSurveyBy(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyBy());
                        //surveyHistoryDto.setAssetSurveyBy(userService.getUserById(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyBy()).getFullname());
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

    public byte[] exportAssetsToExcel(String classificationStr) throws IOException {
        AssetClassification assetClassification = null;

        // 문자열을 AssetClassification Enum으로 변환
        if (classificationStr != null && !classificationStr.isEmpty()) {
            try {
                assetClassification = AssetClassification.valueOf(classificationStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid asset classification");
            }
        }

        List<CommonAsset> assets = listAssetByExcel(assetClassification); // 자산 목록 조회 메소드
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Workbook workbook = new XSSFWorkbook()) {

        // 분류별로 시트 생성
        for (AssetClassification classification : AssetClassification.values()) {
            Sheet sheet = workbook.createSheet(classification.getDescription());

            // 병합 셀 생성 및 제목 설정
            mergeCellsAndSetTitle(sheet);

            // 정보자산 중요성 등급 테이블 추가
            addImportanceGradeInfo(sheet);

            // 헤더 생성
            createHeaderRow(sheet, classification); // 분류 전달

            // 데이터 추가 후 가장 긴 길이를 저장할 배열
            int[] maxColumnLengths = new int[41]; // 0부터 18까지 총 19개의 열

            // 자산별 데이터 행 추가
            addAssetDataRows(sheet, assets, classification, maxColumnLengths);

            // 열 너비 조정
            adjustColumnWidths(sheet, maxColumnLengths);

        }
            // 응답에 엑셀 파일 작성
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeaderRow(Sheet sheet, AssetClassification classification) {
        Row headerRow = sheet.createRow(8);
        String[] headers = {
                "No", "자산 기준", "자산 코드", "자산명", "자산분류",
                "목적/기능", "자산 위치", "부서", "사용자", "소유자",
                "보안담당자", "사용상태", "가동여부", "도입일자", "기밀성",
                "무결성", "가용성", "중요성점수", "중요성등급", "비고",
                "구매비용", "구매날짜", "유지기간", "내용연수", "감각상각방법",
                "구입처", "구입처 연락처", "취득경로", "잔존가치", "현재가치"
        };

        // 기본 헤더 설정
        for (int i = 0; i < headers.length; i++) {
            createHeaderCell(headerRow, i, headers[i]);
        }
        // 각 자산 분류에 따른 추가 헤더 제목 설정
        addClassificationHeaders(headerRow, classification);
    }
    private void addClassificationHeaders(Row headerRow, AssetClassification classification) {
        String[][] classificationHeaders = {
                { "서비스범위" }, // INFORMATION_PROTECTION_SYSTEM
                { "서비스범위", "OS", "관련 DB", "IP", "화면 수" }, // APPLICATION_PROGRAM
                { "IP", "Sever ID", "Sever PW", "담당업체", "OS" }, // SOFTWARE
                { "OS", "시스템", "DB 종류" }, // ELECTRONIC_INFORMATION
                { "문서 등급", "문서 형태", "문서 링크" }, // DOCUMENT
                { "출원 일자", "등록 일자", "만료 일자", "특허/상표 상태", "출원국가", "특허분류", "특허세목", "출원번호", "발명자", "권리권자", "관련문서" }, // PATENTS_AND_TRADEMARKS
                { "장비 유형", "랙유닛", "전원공급장치", "쿨링시스템", "인터페이스 포트", "폼팩터", "확장슬롯수", "그래픽카드", "포트 구성", "모니터 포함여부" }, // ITSYSTEM_EQUIPMENT
                { "장비 유형", "포트수", "지원프로토콜", "펨웨어 버전", "네트워크 속도", "서비스범위" }, // ITNETWORK_EQUIPMENT
                { "IP", "제품시리얼번호", "OS", "보안관제", "내부정보 유출 방지", "악성코드, 랜섬웨어 탐지", "안티랜섬웨어", "NAC agent" }, // TERMINAL
                { "크기" }, // FURNITURE
                { "기기 유형", "모델번호", "연결방식", "전원사양" }, // DEVICES
                { "배기량", "차량의 문 수", "엔진형식", "차량종류", "차량 식별번호", "차량 색상", "연식" }, // CAR
                { "기타 세부 설명", "사용 빈도" } // OTHERASSETS
        };

        // 추가 헤더 생성
        int startColumnIndex = 32; // 추가 헤더 시작 열 인덱스
        for (String header : classificationHeaders[classification.ordinal()]) {
            createHeaderCell(headerRow, startColumnIndex++, header);
        }
    }

    private void addAssetDataRows(Sheet sheet, List<CommonAsset> assets, AssetClassification classification, int[] maxColumnLengths) {
        int rowIndex = 9;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (CommonAsset asset : assets) {
            if (asset.getAssetClassification() == classification) {
                Row row = sheet.createRow(rowIndex++);
                // 기본 자산 데이터 설정
                addBasicAssetData(row, asset, formatter);

                // 자산 분류별 데이터 처리
                addClassificationSpecificData(row, asset, classification, formatter);

                // 각 셀의 길이를 기준으로 최대 길이 업데이트
                updateMaxColumnLengths(row, maxColumnLengths);
            }
        }
    }

    private void addBasicAssetData(Row row, CommonAsset asset, DateTimeFormatter formatter) {
        row.createCell(0).setCellValue(asset.getAssetNo());
        row.createCell(1).setCellValue(asset.getAssetBasis().toString());
        row.createCell(2).setCellValue(asset.getAssetCode());
        row.createCell(3).setCellValue(asset.getAssetName());
        row.createCell(4).setCellValue(asset.getAssetClassification().getDescription());
        row.createCell(5).setCellValue(asset.getPurpose());
        row.createCell(6).setCellValue(asset.getAssetLocation().getDescription());
        row.createCell(7).setCellValue(asset.getDepartment().getDescription());
        row.createCell(8).setCellValue(userService.getUserById(asset.getAssetUser()).getFullname());
        row.createCell(9).setCellValue(userService.getUserById(asset.getAssetOwner()).getFullname());
        row.createCell(10).setCellValue(userService.getUserById(asset.getAssetSecurityManager()).getFullname());
        row.createCell(11).setCellValue(asset.getUseStated().getDescription());
        row.createCell(12).setCellValue(asset.getOperationStatus().getDescription());
        row.createCell(13).setCellValue(asset.getIntroducedDate().format(formatter));

        // 기밀성, 무결성, 가용성 처리
        int confidentiality = asset.getConfidentiality();
        int integrity = asset.getIntegrity();
        int availability = asset.getAvailability();
        int totalScore = confidentiality + integrity + availability;

        row.createCell(14).setCellValue(confidentiality);
        row.createCell(15).setCellValue(integrity);
        row.createCell(16).setCellValue(availability);
        row.createCell(17).setCellValue(totalScore);

        // 중요성 등급 계산
        String grade = calculateAssetGrade(totalScore);
        row.createCell(18).setCellValue(grade);

        row.createCell(19).setCellValue(asset.getNote());
        row.createCell(20).setCellValue(asset.getPurchaseCost());
        row.createCell(21).setCellValue(asset.getPurchaseDate().format(formatter));
        row.createCell(22).setCellValue(asset.getMaintenancePeriod().format(formatter));
        row.createCell(23).setCellValue(asset.getUsefulLife());
        row.createCell(24).setCellValue(asset.getDepreciationMethod().getDescription());
        row.createCell(25).setCellValue(asset.getPurchaseSource());
        row.createCell(26).setCellValue(asset.getContactInformation());
        row.createCell(27).setCellValue(asset.getAcquisitionRoute());
        row.createCell(28).setCellValue(asset.getNote());
        row.createCell(29).setCellValue(asset.getNote());
    }

    private String calculateAssetGrade(int totalScore) {
        if (totalScore >= 3 && totalScore <= 4) {
            return "C등급";
        } else if (totalScore >= 5 && totalScore <= 6) {
            return "B등급";
        } else if (totalScore >= 7 && totalScore <= 9) {
            return "A등급";
        } else {
            return "등급 없음"; // 기본값 설정
        }
    }


    private void addClassificationSpecificData(Row row, CommonAsset asset, AssetClassification classification, DateTimeFormatter formatter) {
        //자산 분류에 따라 추가적인 정보 처리
        switch (classification) {
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(informationProtectionSystem.getServiceScope());
            }
            case APPLICATION_PROGRAM -> {
                ApplicationProgram applicationProgram = applicationProgramRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(applicationProgram.getServiceScope());
                row.createCell(33).setCellValue(applicationProgram.getOs());
                row.createCell(34).setCellValue(applicationProgram.getRelatedDB());
                row.createCell(35).setCellValue(applicationProgram.getIp());
                row.createCell(36).setCellValue(applicationProgram.getOs());
            }
            case SOFTWARE -> {
                Software software = softwareRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(software.getIp());
                row.createCell(33).setCellValue(software.getServerId());
                row.createCell(34).setCellValue(software.getServerPassword());
                row.createCell(35).setCellValue(software.getCompanyManager());
                row.createCell(36).setCellValue(software.getOs());
            }
            case ELECTRONIC_INFORMATION -> {
                ElectronicInformation electronicInformation = electronicInformationRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(electronicInformation.getOs());
                row.createCell(33).setCellValue(electronicInformation.getSystem());
                row.createCell(34).setCellValue(electronicInformation.getDbtype());
            }
            case DOCUMENT -> {
                Document document = documentRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(document.getDocumentGrade().getDescription());
                row.createCell(33).setCellValue(document.getDocumentType().getDescription());
                row.createCell(34).setCellValue(document.getDocumentLink());
            }
            case PATENTS_AND_TRADEMARKS -> {
                PatentsAndTrademarks patentsAndTrademarks = patentsAndTrademarksRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(patentsAndTrademarks.getApplicationDate().format(formatter));
                row.createCell(33).setCellValue(patentsAndTrademarks.getRegistrationDate().format(formatter));
                row.createCell(34).setCellValue(patentsAndTrademarks.getExpirationDate().format(formatter));
                row.createCell(35).setCellValue(patentsAndTrademarks.getPatentTrademarkStatus().getDescription());
                row.createCell(36).setCellValue(patentsAndTrademarks.getCountryApplication().getDescription());
                row.createCell(37).setCellValue(patentsAndTrademarks.getPatentClassification().getDescription());
                row.createCell(38).setCellValue(patentsAndTrademarks.getPatentItem().getDescription());
                row.createCell(39).setCellValue(patentsAndTrademarks.getApplicationNo());
                row.createCell(40).setCellValue(patentsAndTrademarks.getInventor());
                row.createCell(41).setCellValue(patentsAndTrademarks.getAssignee());
                //row.createCell(42).setCellValue(patentsAndTrademarks.getR());

            }
            case ITSYSTEM_EQUIPMENT -> {
                ItSystemEquipment itSystemEquipment = itSystemEquipmentRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(itSystemEquipment.getEquipmentType());
                //row.createCell(33).setCellValue(itSystemEquipment.get());
                row.createCell(34).setCellValue(itSystemEquipment.getPowerSupply());
                row.createCell(35).setCellValue(itSystemEquipment.getCoolingSystem());
                row.createCell(36).setCellValue(itSystemEquipment.getInterfacePorts());
                row.createCell(37).setCellValue(itSystemEquipment.getFormFactor());
                row.createCell(38).setCellValue(itSystemEquipment.getExpansionSlots());
                row.createCell(39).setCellValue(itSystemEquipment.getGraphicsCard());
                row.createCell(40).setCellValue(itSystemEquipment.getPortConfiguration());
                row.createCell(41).setCellValue(itSystemEquipment.getMonitorIncluded());
            }
            case ITNETWORK_EQUIPMENT -> {
                ItNetworkEquipment itNetworkEquipment = itNetworkEquipmentRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(itNetworkEquipment.getEquipmentType());
                row.createCell(33).setCellValue(itNetworkEquipment.getNumberOfPorts());
                row.createCell(34).setCellValue(itNetworkEquipment.getSupportedProtocols());
                row.createCell(35).setCellValue(itNetworkEquipment.getFirmwareVersion());
                row.createCell(36).setCellValue(itNetworkEquipment.getNetworkSpeed());
                row.createCell(37).setCellValue(itNetworkEquipment.getServiceScope());
            }
            case TERMINAL -> {
                Terminal terminal = terminalRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(terminal.getIp());
                //row.createCell(33).setCellValue(terminal.());
                row.createCell(34).setCellValue(terminal.getOs());
                row.createCell(35).setCellValue(terminal.getSecurityControl().getDescription());
                row.createCell(36).setCellValue(terminal.getKaitsKeeper().format(formatter));
                row.createCell(37).setCellValue(terminal.getV3OfficeSecurity().format(formatter));
                row.createCell(38).setCellValue(terminal.getAppCheckPro().format(formatter));
                row.createCell(39).setCellValue(terminal.getTgate().format(formatter));
            }
            case FURNITURE -> {
                Furniture furniture = furnitureRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(furniture.getFurnitureSize());
            }
            case DEVICES -> {
                Devices devices = devicesRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(devices.getDeviceType());
                row.createCell(33).setCellValue(devices.getModelNumber());
                row.createCell(34).setCellValue(devices.getConnectionType());
                row.createCell(35).setCellValue(devices.getPowerSpecifications());
            }
            case CAR -> {
                Car car = carRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(car.getDisplacement());
                row.createCell(33).setCellValue(car.getDoorsCount());
                row.createCell(34).setCellValue(car.getEngineType().getDescription());
                row.createCell(35).setCellValue(car.getCarType().getDescription());
                row.createCell(36).setCellValue(car.getIdentificationNo());
                row.createCell(37).setCellValue(car.getCarColor());
                row.createCell(38).setCellValue(car.getModelYear());
            }
            case OTHERASSETS -> {
                OtherAssets otherAssets = otherAssetsRepository.findByAssetNo(asset);
                row.createCell(32).setCellValue(otherAssets.getOtherDescription());
                row.createCell(33).setCellValue(otherAssets.getUsageFrequency());
            }
            // 나머지 분류별 처리 추가...
        }
    }

    private void updateMaxColumnLengths(Row row, int[] maxColumnLengths) {
        for (int c = 0; c <= 40; c++) {
            Cell cell = row.getCell(c);
            if (cell != null) {
                String cellValue = cell.toString(); // 모든 셀 값을 문자열로 변환
                maxColumnLengths[c] = Math.max(maxColumnLengths[c], cellValue.length());
            }
        }
    }
    private void adjustColumnWidths(Sheet sheet, int[] maxColumnLengths) {
        for (int i = 0; i <= 39; i++) {
            sheet.setColumnWidth(i, (maxColumnLengths[i] + 4) * 256); // +4는 여백을 위한 것
        }
    }
    
    // 일단 셀병합 부터 빼기
    private void mergeCellsAndSetTitle(Sheet sheet) {

        Row titleRow = sheet.createRow(7);  // 7번째 행 생성

        // 기존 병합 셀들
        Cell titleCell = titleRow.createCell(0);  // 첫 번째 셀
        titleCell.setCellValue("필수입력사항");

        Cell titleCell1 = titleRow.createCell(14);  // 14번째 셀
        titleCell1.setCellValue("자산 중요성 평가항목");

        Cell titleCell2 = titleRow.createCell(21);  // 21번째 셀
        titleCell2.setCellValue("재무 및 구매 정보");

        // 셀 병합 : A8부터 K8, O8부터 U8, V8부터 AF8까지 병합
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 10));   // A8-K8 병합
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 14, 19));  // O8-U8 병합
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 21, 30));  // V8-AF8 병합

        // 셀 스타일 생성
        CellStyle titleStyle = createTitleStyle(sheet);

        // 스타일 적용 (각 병합된 셀에 동일한 스타일 적용)
        titleCell.setCellStyle(titleStyle);
        titleCell1.setCellStyle(titleStyle);
        titleCell2.setCellStyle(titleStyle);

        // 새로운 병합 셀 (D2-R5 범위)
        Cell mergedTitleCell = sheet.createRow(1).createCell(3);  // D2 위치에 셀 생성
        mergedTitleCell.setCellValue("정보자산 목록 및 중요성 평가");

        // 셀 병합 : D2-R5 병합
        sheet.addMergedRegion(new CellRangeAddress(1, 4, 3, 17));  // D2-R5 범위 병합

        // 병합된 셀에 동일한 스타일 적용
        mergedTitleCell.setCellStyle(titleStyle);
    }


    private CellStyle createTitleStyle(Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true); // 글씨를 굵게
        titleFont.setFontHeightInPoints((short) 9); // 폰트 크기 9
        titleFont.setFontName("맑은 고딕"); // 폰트 "맑은 고딕"
        titleStyle.setFont(titleFont);

        // 배경 색상 설정 (RGB 217, 217, 217)
        XSSFColor backgroundColor = new XSSFColor(new java.awt.Color(217, 217, 217), null);
        ((XSSFCellStyle) titleStyle).setFillForegroundColor(backgroundColor);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 배경색 채우기
        // 가운데 정렬 추가
        titleStyle.setAlignment(HorizontalAlignment.CENTER); // 가로 방향 가운데 정렬
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 방향 가운데 정렬

        return titleStyle;
    }

    private void createHeaderCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);

        Workbook workbook = row.getSheet().getWorkbook();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 9); // 폰트 크기 9
        font.setFontName("맑은 고딕"); // 폰트 "맑은 고딕"

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        // 배경 색상 설정 (RGB 217, 217, 217)
        XSSFColor backgroundColor = new XSSFColor(new java.awt.Color(217, 217, 217), null);
        ((XSSFCellStyle) style).setFillForegroundColor(backgroundColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 배경색 채우기

        // 가운데 정렬 추가
        style.setAlignment(HorizontalAlignment.CENTER); // 가로 방향 가운데 정렬
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 방향 가운데 정렬

        cell.setCellStyle(style);
        // 기본 행 높이 설정 (엑셀 기준 30포인트)
        row.setHeightInPoints(30);

    }

    private void addImportanceGradeInfo(Sheet sheet) {
        // 정보자산 중요성 등급 설정
        Row row2 = sheet.createRow(1);
        Cell cellS2 = row2.createCell(18);
        cellS2.setCellValue("※정보자산 중요성 등급");

        // S3~T6에 점수, 등급 입력
        String[][] gradeData = {
                {"점수", "등급"},
                {"7 ~ 9 점", "A 급"},
                {"5 ~ 6 점", "B 급"},
                {"3 ~ 4 점", "C 급"}
        };

        // 폰트 스타일 생성 (맑은 고딕, 사이즈 8)
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("맑은 고딕");
        font.setFontHeightInPoints((short) 8);

        // 셀 스타일 생성
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER); // 가로 방향 가운데 정렬
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 방향 가운데 정렬

        for (int i = 0; i < gradeData.length; i++) {
            Row row = sheet.createRow(2 + i);
            for (int j = 0; j < gradeData[i].length; j++) {
                Cell cell = row.createCell(18 + j); // S 열은 18번째, T 열은 19번째
                cell.setCellValue(gradeData[i][j]);
                cell.setCellStyle(style); // 스타일 적용
            }
        }
    }


    public List<CommonAsset> listAssetByExcel(AssetClassification assetClassification) {
        // 자산 분류가 null인지 확인하고 기본값 또는 예외 처리 추가
        if (assetClassification == null) {
            // assetClassification이 null일 경우 전체 자산 조회
            return commonAssetRepository.findAssetByExcel(null); // null을 넘겨서 전체 조회를 처리하도록
        } else {
            // assetClassification이 있을 경우 해당 분류에 따라 자산 목록을 조회
            return commonAssetRepository.findAssetByExcel(assetClassification);
        }

    }

    //  DeleteHistory의 폐기 이력 List 보여주기 -- 폐기자 (접속자)까지 넣었음
     public List<DeleteHistoryDto> getDeleteHistory() {
        List<DemandDtl> deleteList = demandDtlRepository.findDeleteHistory();

        List<DeleteHistoryDto> deleteHistoryList = new ArrayList<>();

        for (DemandDtl demandDtl : deleteList) {
            DeleteHistoryDto dto = new DeleteHistoryDto();
            CommonAsset asset = demandDtl.getAssetNo();
            Demand demand = demandDtl.getDemandNo();

            dto.setAssetCode(asset.getAssetCode());
            dto.setAssetName(asset.getAssetName());
            dto.setDeleteBy(userService.getUserById(demand.getDemandBy()).getFullname());
            dto.setDeleteReason(demand.getDemandReason());
            dto.setDeleteDetail(demand.getDemandDetail());
            dto.setDeleteMethod(demand.getDisposeMethod());
            dto.setDeleteLocation(demand.getDisposeLocation());
            dto.setDeleteDate(demand.getDemandDate());

            deleteHistoryList.add(dto);
        }

        return deleteHistoryList;
    }

    // UpdateHistory 의 수정 이력 List 보여주기 - 수정자 (접속자) 까지 넣었음
    public List<UpdateHistoryDto> getUpDateHistory() {
        List<DemandDtl> updateList = demandDtlRepository.findUpdateHistory();

        List<UpdateHistoryDto> updateHistoryDtoList = new ArrayList<>();

        for (DemandDtl demandDtl : updateList) {
            UpdateHistoryDto dto = new UpdateHistoryDto();
            CommonAsset asset = demandDtl.getAssetNo();
            Demand demand = demandDtl.getDemandNo();
            dto.setAssetNo(asset.getAssetNo());
            dto.setAssetCode(asset.getAssetCode());
            dto.setAssetName(asset.getAssetName());
            dto.setUpdateBy(userService.getUserById(demand.getDemandBy()).getFullname());
            dto.setUpdateReason(demand.getDemandReason());
            dto.setUpdateDetail(demand.getDemandDetail());
            dto.setUpdateDate(demand.getDemandDate());

            updateHistoryDtoList.add(dto);
        }
        return updateHistoryDtoList;
    }

    // UpdateHistory 의 수정 이력에서 modal 창을 눌렀을 시, approve의 기존 자산과 바뀐 자산 2개 보여주기
    public List<AssetDto> getLatestAndPreviousAssets(Long assetNo) {

        // 자산코드 (assetCode)로  최신 자산(수정) 과 그 이전 자산 가져오기
        List<CommonAsset> assets = commonAssetRepository.findNextAssetsByAssetNo(assetNo);
        System.out.println("ssss"+assets.size());
        if (assets.size() < 2) {
            throw new RuntimeException("이전 자산 정보를 찾을 수 없습니다.");
        }
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
                    .assetUser(userService.getUserById(asset.getAssetUser()).getFullname())
                    .assetOwner(userService.getUserById(asset.getAssetOwner()).getFullname())
                    .assetSecurityManager(userService.getUserById(asset.getAssetSecurityManager()).getFullname())
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


    // 자산 폐기 동작 (자산 하나 폐기) - ADMIN 권한임
    public AssetUpdateResponse  DisposeAsset(String assetCode, AssetDisposeDto assetDisposeDto) {

        // 기존 입력되어 있는 assetCode 조회
        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode)
                .orElseThrow(() -> new RuntimeException("자산코드를 찾을 수 없음: " + assetCode));

        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.TRUE);
        commonAssetRepository.save(commonAsset);

        // 폐기 이력 저장
        Demand demand = new Demand();
        demand.setDemandBy(assetDisposeDto.getDisposeUser());
        demand.setDemandDate(LocalDate.now()); // 폐기 일자 - 추후 자동생성 변경
        demand.setDemandReason(assetDisposeDto.getDisposeReason()); // 폐기 사유
        demand.setDemandDetail(assetDisposeDto.getDisposeDetail()); // 폐기내용
        demand.setDisposeMethod(assetDisposeDto.getDisposeMethod()); // 폐기 방법
        demand.setDisposeLocation(assetDisposeDto.getDisposeLocation());  // 폐기 위치
        demandRepository.save(demand);

        // DemandDtl 테이블에 저장
        DemandDtl demandDtl = new DemandDtl();
        demandDtl.setAssetNo(commonAsset);  // CommonAsset과 연관 설정
        demandDtl.setDemandNo(demand);  // Demand와 연관 설정
        demandDtlRepository.save(demandDtl); // DemandDtl 테이블에 저장
        //return commonAsset;
        // 자산 폐기 성공 메시지 반환
        return new AssetUpdateResponse("자산 폐기 등록 완료: " + commonAsset.getAssetNo(), commonAsset.getAssetNo());
    }

}
