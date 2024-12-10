package com.codehows.taelim.service;


import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.*;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
            AmountSetDto amountSetDto, // 전달받는 AmountSet의 ID
            Ownership ownership,
            OperationStatus operationStatus,
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
                    amountSetDto, // 전달받는 AmountSet의 ID
                    ownership,
                    operationStatus,
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
                    amountSetDto, // 전달받는 AmountSet의 ID
                    ownership,
                    operationStatus,
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

//            // assetUser, assetOwner, assetSecurityManager가 null일 경우 처리
//            UserDto userAsset  = userService.getUserById(commonAsset.getAssetUser());
//            assetDto.setAssetUser(userAsset  != null ? userAsset.getFullname() : "Unknown User");
//
//            UserDto assetOwner = userService.getUserById(commonAsset.getAssetOwner());
//            assetDto.setAssetOwner(assetOwner != null ? assetOwner.getFullname() : "Unknown Owner");
//
//            UserDto assetSecurityManager = userService.getUserById(commonAsset.getAssetSecurityManager());
//            assetDto.setAssetSecurityManager(assetSecurityManager != null ? assetSecurityManager.getFullname() : "Unknown Manager");
            try {
                UserDto userAsset = userService.getUserById(commonAsset.getAssetUser());
                assetDto.setAssetUser(userAsset != null ? userAsset.getFullname() : null);
            } catch (Exception e) {
                assetDto.setAssetUser(null); // 오류 발생 시 null로 설정
            }

            try {
                UserDto assetOwner = userService.getUserById(commonAsset.getAssetOwner());
                assetDto.setAssetOwner(assetOwner != null ? assetOwner.getFullname() : null);
            } catch (Exception e) {
                assetDto.setAssetOwner(null); // 오류 발생 시 null로 설정
            }

            try {
                UserDto assetSecurityManager = userService.getUserById(commonAsset.getAssetSecurityManager());
                assetDto.setAssetSecurityManager(assetSecurityManager != null ? assetSecurityManager.getFullname() : null);
            } catch (Exception e) {
                assetDto.setAssetSecurityManager(null); // 오류 발생 시 null로 설정
            }

            assetDto.setAssetUserId(commonAsset.getAssetUser());  // ID 값 저장
            assetDto.setAssetOwnerId(commonAsset.getAssetOwner());  // ID 값 저장
            assetDto.setAssetSecurityManagerId(commonAsset.getAssetSecurityManager());  // ID 값 저장

            assetDto.setProductSerialNumber(commonAsset.getProductSerialNumber());
            assetDto.setQuantity(commonAsset.getQuantity());
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
                        assetDto.setRackUnit(itSystemEquipment.getRackUnit());
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
                    .filter(file -> file.getFileName() != null && !file.getFileName().isEmpty()) // file_name이 null이 아니고 빈 문자열이 아닐 경우에만 복사
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
                        repairHistoryDto.setRepairBy(userService.getUserById(repairHistory.getRepairBy()).getFullname());
                        repairHistoryDto.setRepairStatus(repairHistory.getRepairStatus());
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
                        surveyHistoryDto.setAssetSurveyBy(userService.getUserById(assetSurveyDetail.getAssetSurveyNo().getAssetSurveyBy()).getFullname());
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
            AssetClassification[] classifications = AssetClassification.values();
            for (int i = 0; i < classifications.length; i++) {
                AssetClassification classification = classifications[i];

                // 인덱스를 기반으로 시트 제목을 설정
                String sheetTitle = "2." + (i + 1) + " "+ classification.getDescription(); // 예: "1. 자산 설명"

                Sheet sheet = workbook.createSheet(sheetTitle);

            setTitleRow(sheet); // 새로운 메서드 호출

            // 병합 셀 생성 및 제목 설정
            mergeCellsAndSetTitle(sheet);

            // 정보자산 중요성 등급 테이블 추가
            addImportanceGradeInfo(sheet);

            // 헤더 생성
            createHeaderRow(sheet, classification); // 분류 전달

            // 데이터 추가 후 가장 긴 길이를 저장할 배열
            int[] maxColumnLengths = new int[45]; // 0부터 18까지 총 19개의 열

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
                "제조사","목적/기능", "자산 위치", "부서", "사용자", "소유자",
                "보안담당자", "수량", "제품시리얼번호", "소유권", "사용상태", "가동여부", "도입일자", "비고",
                "기밀성","무결성","가용성", "중요성점수", "중요성등급",
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
                { "서비스범위", "사용OS", "관련 DB", "IP", "화면 수" }, // APPLICATION_PROGRAM
                { "IP", "Sever ID", "Sever PW", "담당업체", "OS" }, // SOFTWARE
                { "사용OS", "시스템", "DB 종류" }, // ELECTRONIC_INFORMATION
                { "문서 등급", "문서 형태", "문서 링크" }, // DOCUMENT
                { "출원 일자", "등록 일자", "만료 일자", "특허/상표 상태", "출원국가", "특허분류", "특허세목", "출원번호", "발명자", "권리권자", "관련문서" }, // PATENTS_AND_TRADEMARKS
                { "장비 유형", "랙유닛", "전원공급장치", "쿨링시스템", "인터페이스 포트", "폼팩터", "확장슬롯수", "그래픽카드", "포트 구성", "모니터 포함여부" }, // ITSYSTEM_EQUIPMENT
                { "장비 유형", "포트수", "지원프로토콜", "펨웨어 버전", "네트워크 속도", "서비스범위" }, // ITNETWORK_EQUIPMENT
                { "IP", "제품시리얼번호", "사용OS", "보안관제", "내부정보 유출 방지", "악성코드, 랜섬웨어 탐지", "안티랜섬웨어", "NAC agent" }, // TERMINAL
                { "크기" }, // FURNITURE
                { "기기 유형", "모델번호", "연결방식", "전원사양" }, // DEVICES
                { "배기량", "차량의 문 수", "엔진형식", "차량종류", "차량 식별번호", "차량 색상", "연식" }, // CAR
                { "기타 세부 설명", "사용 빈도" } // OTHERASSETS
        };

        // 추가 헤더 생성
        int startColumnIndex = 35; // 추가 헤더 시작 열 인덱스
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

    private CellStyle createCellStyle(Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();

        // 셀 스타일 생성
        CellStyle cellStyle = workbook.createCellStyle();

        // 폰트 설정
        Font font = workbook.createFont();
        font.setFontName("맑은 고딕");
        font.setFontHeightInPoints((short) 9);
        cellStyle.setFont(font);

        // 정렬 설정
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 테두리 설정
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }


    private void addBasicAssetData(Row row, CommonAsset asset, DateTimeFormatter formatter) {
        CellStyle cellStyle = createCellStyle(row.getSheet()); // 셀 스타일 생성

        row.createCell(0).setCellValue(asset.getAssetNo() != null ? asset.getAssetNo() : 0);
        row.getCell(0).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(1).setCellValue(asset.getAssetBasis() != null ? asset.getAssetBasis().getDescription() : "");
        row.getCell(1).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(2).setCellValue(asset.getAssetCode() != null ? asset.getAssetCode() : "");
        row.getCell(2).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(3).setCellValue(asset.getAssetName() != null ? asset.getAssetName() : "");
        row.getCell(3).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(4).setCellValue(asset.getAssetClassification() != null ? asset.getAssetClassification().getDescription() : "");
        row.getCell(4).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(5).setCellValue(asset.getManufacturingCompany() != null ? asset.getManufacturingCompany() : "");
        row.getCell(5).setCellStyle(cellStyle);
        row.createCell(6).setCellValue(asset.getPurpose() != null ? asset.getPurpose() : "");
        row.getCell(6).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(7).setCellValue(asset.getAssetLocation() != null ? asset.getAssetLocation().getDescription() : "");
        row.getCell(7).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(8).setCellValue(asset.getDepartment() != null ? asset.getDepartment().getDescription() : "");
        row.getCell(8).setCellStyle(cellStyle); // 스타일 적용

        UserDto user = userService.getUserById(asset.getAssetUser());
        row.createCell(9).setCellValue(user != null ? user.getFullname() : ""); // String 타입
        row.getCell(9).setCellStyle(cellStyle); // 스타일 적용

        UserDto owner = userService.getUserById(asset.getAssetOwner());
        row.createCell(10).setCellValue(owner != null ? owner.getFullname() : ""); // String 타입
        row.getCell(10).setCellStyle(cellStyle); // 스타일 적용

        UserDto securityManager = userService.getUserById(asset.getAssetSecurityManager());
        row.createCell(11).setCellValue(securityManager != null ? securityManager.getFullname() : ""); // String 타입
        row.getCell(11).setCellStyle(cellStyle); // 스타일 적용

        row.createCell(12).setCellValue(asset.getQuantity() != null ? asset.getQuantity() : 0);
        row.getCell(12).setCellStyle(cellStyle);
        row.createCell(13).setCellValue(asset.getProductSerialNumber() != null ? asset.getProductSerialNumber() : "");
        row.getCell(13).setCellStyle(cellStyle);
        row.createCell(14).setCellValue(asset.getOwnership() != null ? asset.getOwnership().getDescription() : "");
        row.getCell(14).setCellStyle(cellStyle);
        row.createCell(15).setCellValue(asset.getUseStated() != null ? asset.getUseStated().getDescription() : "");
        row.getCell(15).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(16).setCellValue(asset.getOperationStatus() != null ? asset.getOperationStatus().getDescription() : "");
        row.getCell(16).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(17).setCellValue(asset.getIntroducedDate() != null ? asset.getIntroducedDate().format(formatter) : "");
        row.getCell(17).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(18).setCellValue(asset.getNote() != null ? asset.getNote() : "");
        row.getCell(18).setCellStyle(cellStyle); // 스타일 적용

        // 기밀성, 무결성, 가용성 처리
        int confidentiality = asset.getConfidentiality();
        int integrity = asset.getIntegrity();
        int availability = asset.getAvailability();
        int totalScore = confidentiality + integrity + availability;

        row.createCell(19).setCellValue(confidentiality);
        row.getCell(19).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(20).setCellValue(integrity);
        row.getCell(20).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(21).setCellValue(availability);
        row.getCell(21).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(22).setCellValue(totalScore);
        row.getCell(22).setCellStyle(cellStyle); // 스타일 적용

        // 중요성 등급 계산
        String grade = calculateAssetGrade(totalScore);
        row.createCell(23).setCellValue(grade != null ? grade : ""); // grade가 null인 경우 빈 문자열로 처리
        row.getCell(23).setCellStyle(cellStyle); // 스타일 적용


        row.createCell(24).setCellValue(asset.getPurchaseCost() != null ? asset.getPurchaseCost() : 0);
        row.getCell(24).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(25).setCellValue(asset.getPurchaseDate() != null ? asset.getPurchaseDate().format(formatter) : "");
        row.getCell(25).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(26).setCellValue(asset.getMaintenancePeriod() != null ? asset.getMaintenancePeriod().format(formatter) : "");
        row.getCell(26).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(27).setCellValue(asset.getUsefulLife() != null ? asset.getUsefulLife() : 0);
        row.getCell(27).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(28).setCellValue(asset.getDepreciationMethod() != null ? asset.getDepreciationMethod().getDescription() : "");
        row.getCell(28).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(29).setCellValue(asset.getPurchaseSource() != null ? asset.getPurchaseSource() : "");
        row.getCell(29).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(30).setCellValue(asset.getContactInformation() != null ? asset.getContactInformation() : "");
        row.getCell(30).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(31).setCellValue(asset.getAcquisitionRoute() != null ? asset.getAcquisitionRoute() : "");
        row.getCell(31).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(32).setCellValue(asset.getNote() != null ? asset.getNote() : "");
        row.getCell(32).setCellStyle(cellStyle); // 스타일 적용
        row.createCell(33).setCellValue(asset.getNote() != null ? asset.getNote() : "");
        row.getCell(33).setCellStyle(cellStyle); // 스타일 적용
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
        CellStyle cellStyle = createCellStyle(row.getSheet()); // 셀 스타일 생성


        //자산 분류에 따라 추가적인 정보 처리
        switch (classification) {
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        informationProtectionSystem.getServiceScope() != null ? informationProtectionSystem.getServiceScope() : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용
            }
            case APPLICATION_PROGRAM -> {
                ApplicationProgram applicationProgram = applicationProgramRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue( applicationProgram.getServiceScope() != null ? applicationProgram.getServiceScope() : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용
                row.createCell(36).setCellValue(applicationProgram.getOs() != null ? applicationProgram.getOs() : ""
                );
                row.getCell(36).setCellStyle(cellStyle); // 스타일 적용
                row.createCell(37).setCellValue(applicationProgram.getRelatedDB() != null ? applicationProgram.getRelatedDB() : ""
                );
                row.getCell(37).setCellStyle(cellStyle); // 스타일 적용
                row.createCell(38).setCellValue(applicationProgram.getIp() != null ? applicationProgram.getIp() : ""
                );
                row.getCell(38).setCellStyle(cellStyle); // 스타일 적용
                row.createCell(39).setCellValue(applicationProgram.getScreenNumber() != null ? applicationProgram.getScreenNumber() : 0 );
                row.getCell(39).setCellStyle(cellStyle); // 스타일 적용
            }
            case SOFTWARE -> {
                Software software = softwareRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        software.getIp() != null ? software.getIp() : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(36).setCellValue(
                        software.getServerId() != null ? software.getServerId() : ""
                );
                row.getCell(36).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(37).setCellValue(
                        software.getServerPassword() != null ? software.getServerPassword() : ""
                );
                row.getCell(37).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(38).setCellValue(
                        software.getCompanyManager() != null ? software.getCompanyManager() : ""
                );
                row.getCell(38).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(39).setCellValue(
                        software.getOs() != null ? software.getOs() : ""
                );
                row.getCell(39).setCellStyle(cellStyle); // 스타일 적용
            }
            case ELECTRONIC_INFORMATION -> {
                ElectronicInformation electronicInformation = electronicInformationRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        electronicInformation.getOs() != null ? electronicInformation.getOs() : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(36).setCellValue(
                        electronicInformation.getSystem() != null ? electronicInformation.getSystem() : ""
                );
                row.getCell(36).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(37).setCellValue(
                        electronicInformation.getDbtype() != null ? electronicInformation.getDbtype() : ""
                );
                row.getCell(37).setCellStyle(cellStyle); // 스타일 적용
            }
            case DOCUMENT -> {
                Document document = documentRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        document.getDocumentGrade() != null ? document.getDocumentGrade().getDescription() : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(36).setCellValue(
                        document.getDocumentType() != null ? document.getDocumentType().getDescription() : ""
                );
                row.getCell(36).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(37).setCellValue(
                        document.getDocumentLink() != null ? document.getDocumentLink() : ""
                );
                row.getCell(37).setCellStyle(cellStyle); // 스타일 적용
            }
            case PATENTS_AND_TRADEMARKS -> {
                PatentsAndTrademarks patentsAndTrademarks = patentsAndTrademarksRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        patentsAndTrademarks.getApplicationDate() != null ? patentsAndTrademarks.getApplicationDate().format(formatter) : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(36).setCellValue(
                        patentsAndTrademarks.getRegistrationDate() != null ? patentsAndTrademarks.getRegistrationDate().format(formatter) : ""
                );
                row.getCell(36).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(37).setCellValue(
                        patentsAndTrademarks.getExpirationDate() != null ? patentsAndTrademarks.getExpirationDate().format(formatter) : ""
                );
                row.getCell(37).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(38).setCellValue(
                        patentsAndTrademarks.getPatentTrademarkStatus() != null ? patentsAndTrademarks.getPatentTrademarkStatus().getDescription() : ""
                );
                row.getCell(38).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(39).setCellValue(
                        patentsAndTrademarks.getCountryApplication() != null ? patentsAndTrademarks.getCountryApplication().getDescription() : ""
                );
                row.getCell(39).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(40).setCellValue(
                        patentsAndTrademarks.getPatentClassification() != null ? patentsAndTrademarks.getPatentClassification().getDescription() : ""
                );
                row.getCell(40).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(41).setCellValue(
                        patentsAndTrademarks.getPatentItem() != null ? patentsAndTrademarks.getPatentItem().getDescription() : ""
                );
                row.getCell(41).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(42).setCellValue(
                        patentsAndTrademarks.getApplicationNo() != null ? patentsAndTrademarks.getApplicationNo() : ""
                );
                row.getCell(42).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(43).setCellValue(
                        patentsAndTrademarks.getInventor() != null ? patentsAndTrademarks.getInventor() : ""
                );
                row.getCell(43).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(44).setCellValue(
                        patentsAndTrademarks.getAssignee() != null ? patentsAndTrademarks.getAssignee() : ""
                );
                row.getCell(44).setCellStyle(cellStyle); // 스타일 적용
            }
            case ITSYSTEM_EQUIPMENT -> {
                ItSystemEquipment itSystemEquipment = itSystemEquipmentRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        itSystemEquipment.getEquipmentType() != null ? itSystemEquipment.getEquipmentType() : ""
                );
                row.getCell(35).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(36).setCellValue(
                        itSystemEquipment.getRackUnit() != null ? itSystemEquipment.getRackUnit() : 0
                );
                row.getCell(36).setCellStyle(cellStyle);

                row.createCell(37).setCellValue(
                        itSystemEquipment.getPowerSupply() != null ? itSystemEquipment.getPowerSupply() : ""
                );
                row.getCell(37).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(38).setCellValue(
                        itSystemEquipment.getCoolingSystem() != null ? itSystemEquipment.getCoolingSystem() : ""
                );
                row.getCell(38).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(39).setCellValue(
                        itSystemEquipment.getInterfacePorts() != null ? itSystemEquipment.getInterfacePorts() : ""
                );
                row.getCell(39).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(40).setCellValue(
                        itSystemEquipment.getFormFactor() != null ? itSystemEquipment.getFormFactor() : ""
                );
                row.getCell(40).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(41).setCellValue(
                        itSystemEquipment.getExpansionSlots() != null ? itSystemEquipment.getExpansionSlots() : 0
                );
                row.getCell(41).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(42).setCellValue(
                        itSystemEquipment.getGraphicsCard() != null ? itSystemEquipment.getGraphicsCard() : ""
                );
                row.getCell(42).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(43).setCellValue(
                        itSystemEquipment.getPortConfiguration() != null ? itSystemEquipment.getPortConfiguration() : ""
                );
                row.getCell(43).setCellStyle(cellStyle); // 스타일 적용

                row.createCell(44).setCellValue(
                        itSystemEquipment.getMonitorIncluded() != null ?
                                (itSystemEquipment.getMonitorIncluded() ? "Yes" : "No") : ""
                );
                row.getCell(44).setCellStyle(cellStyle); // 스타일 적용
            }
            case ITNETWORK_EQUIPMENT -> {
                ItNetworkEquipment itNetworkEquipment = itNetworkEquipmentRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        itNetworkEquipment.getEquipmentType() != null ? itNetworkEquipment.getEquipmentType() : ""
                );
                row.getCell(35).setCellStyle(cellStyle);
                row.createCell(36).setCellValue(
                        itNetworkEquipment.getNumberOfPorts() != null ? itNetworkEquipment.getNumberOfPorts() : 0
                );
                row.getCell(36).setCellStyle(cellStyle);
                row.createCell(37).setCellValue(
                        itNetworkEquipment.getSupportedProtocols() != null ? itNetworkEquipment.getSupportedProtocols() : ""
                );
                row.getCell(37).setCellStyle(cellStyle);
                row.createCell(38).setCellValue(
                        itNetworkEquipment.getFirmwareVersion() != null ? itNetworkEquipment.getFirmwareVersion() : ""
                );
                row.getCell(38).setCellStyle(cellStyle);
                row.createCell(39).setCellValue(
                        itNetworkEquipment.getNetworkSpeed() != null ? itNetworkEquipment.getNetworkSpeed() : 0
                );
                row.getCell(39).setCellStyle(cellStyle);
                row.createCell(40).setCellValue(
                        itNetworkEquipment.getServiceScope() != null ? itNetworkEquipment.getServiceScope() : ""
                );
                row.getCell(40).setCellStyle(cellStyle);
            }
            case TERMINAL -> {
                Terminal terminal = terminalRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(
                        terminal.getIp() != null ? terminal.getIp() : ""
                );
                row.getCell(35).setCellStyle(cellStyle);
                row.createCell(36).setCellValue(
                        terminal.getOs() != null ? terminal.getOs() : ""
                );
                row.getCell(36).setCellStyle(cellStyle);
                row.createCell(37).setCellValue(
                        terminal.getSecurityControl() != null ? terminal.getSecurityControl().getDescription() : ""
                );
                row.getCell(37).setCellStyle(cellStyle);
                row.createCell(38).setCellValue(
                        terminal.getKaitsKeeper() != null ? terminal.getKaitsKeeper().format(formatter) : ""
                );
                row.getCell(38).setCellStyle(cellStyle);
                row.createCell(39).setCellValue(
                        terminal.getV3OfficeSecurity() != null ? terminal.getV3OfficeSecurity().format(formatter) : ""
                );
                row.getCell(39).setCellStyle(cellStyle);
                row.createCell(40).setCellValue(
                        terminal.getAppCheckPro() != null ? terminal.getAppCheckPro().format(formatter) : ""
                );
                row.getCell(40).setCellStyle(cellStyle);
                row.createCell(41).setCellValue(
                        terminal.getTgate() != null ? terminal.getTgate().format(formatter) : ""
                );
                row.getCell(41).setCellStyle(cellStyle);
            }
            case FURNITURE -> {
                Furniture furniture = furnitureRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(furniture.getFurnitureSize() != null ? furniture.getFurnitureSize() : "");
                row.getCell(35).setCellStyle(cellStyle);
            }
            case DEVICES -> {
                Devices devices = devicesRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(devices.getDeviceType() != null ? devices.getDeviceType() : "");
                row.getCell(35).setCellStyle(cellStyle);
                row.createCell(36).setCellValue(devices.getModelNumber() != null ? devices.getModelNumber() : "");
                row.getCell(36).setCellStyle(cellStyle);
                row.createCell(37).setCellValue(devices.getConnectionType() != null ? devices.getConnectionType() : "");
                row.getCell(37).setCellStyle(cellStyle);
                row.createCell(38).setCellValue(devices.getPowerSpecifications() != null ? devices.getPowerSpecifications() : "");
                row.getCell(38).setCellStyle(cellStyle);
            }
            case CAR -> {
                Car car = carRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(car.getDisplacement() != null ? car.getDisplacement() : 0);
                row.getCell(35).setCellStyle(cellStyle);
                row.createCell(36).setCellValue(car.getDoorsCount() != null ? car.getDoorsCount() : 0);
                row.getCell(36).setCellStyle(cellStyle);
                row.createCell(37).setCellValue(car.getEngineType() != null ? car.getEngineType().getDescription() : "");
                row.getCell(37).setCellStyle(cellStyle);
                row.createCell(38).setCellValue(car.getCarType() != null ? car.getCarType().getDescription() : "");
                row.getCell(38).setCellStyle(cellStyle);
                row.createCell(39).setCellValue(car.getIdentificationNo() != null ? car.getIdentificationNo() : "");
                row.getCell(39).setCellStyle(cellStyle);
                row.createCell(40).setCellValue(car.getCarColor() != null ? car.getCarColor() : "");
                row.getCell(40).setCellStyle(cellStyle);
                row.createCell(41).setCellValue(car.getModelYear() != null ? car.getModelYear() : 0);
                row.getCell(41).setCellStyle(cellStyle);
            }
            case OTHERASSETS -> {
                OtherAssets otherAssets = otherAssetsRepository.findByAssetNo(asset);
                row.createCell(35).setCellValue(otherAssets.getOtherDescription() != null ? otherAssets.getOtherDescription() : "");
                row.getCell(35).setCellStyle(cellStyle);
                row.createCell(36).setCellValue(otherAssets.getUsageFrequency() != null ? otherAssets.getUsageFrequency() : "");
                row.getCell(36).setCellStyle(cellStyle);
            }
            // 나머지 분류별 처리 추가...
        }
    }

    private void updateMaxColumnLengths(Row row, int[] maxColumnLengths) {
        for (int c = 0; c <= 45; c++) {
            Cell cell = row.getCell(c);
            if (cell != null) {
                String cellValue = cell.toString(); // 모든 셀 값을 문자열로 변환
                maxColumnLengths[c] = Math.max(maxColumnLengths[c], cellValue.length());
            }
        }
    }
    private void adjustColumnWidths(Sheet sheet, int[] maxColumnLengths) {
        // 열 너비를 자동으로 조정
        for (int i = 0; i < maxColumnLengths.length; i++) {
            // POI의 autoSizeColumn을 사용
            sheet.autoSizeColumn(i);

            // 기본 너비가 너무 좁으면 최소값을 설정
            int currentWidth = sheet.getColumnWidth(i);
            int minWidth = 256 * 10; // 최소 너비 (예: 10자)
            if (currentWidth < minWidth) {
                sheet.setColumnWidth(i, minWidth);
            }
        }
    }


    // 일단 셀병합 부터 빼기
    private void mergeCellsAndSetTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(7);  // 7번째 행 생성

        // 첫 번째 병합 영역: A8-K8
        CellRangeAddress mergedRegion1 = new CellRangeAddress(7, 7, 0, 18);
        Cell titleCell1 = titleRow.createCell(0);
        titleCell1.setCellValue("필수입력사항");
        CellStyle style1 = sheet.getWorkbook().createCellStyle();
        setTitleCellStyle(sheet, style1);  // 스타일 설정
        titleCell1.setCellStyle(style1);
        sheet.addMergedRegion(mergedRegion1);
        applyBordersToMergedRegion(sheet, mergedRegion1);

        // 두 번째 병합 영역: O8-U8
        CellRangeAddress mergedRegion2 = new CellRangeAddress(7, 7, 19, 23);
        Cell titleCell2 = titleRow.createCell(19);
        titleCell2.setCellValue("자산 중요성 평가항목");
        CellStyle style2 = sheet.getWorkbook().createCellStyle();
        setTitleCellStyle(sheet, style2);  // 스타일 설정
        titleCell2.setCellStyle(style2);
        sheet.addMergedRegion(mergedRegion2);
        applyBordersToMergedRegion(sheet, mergedRegion2);

        // 세 번째 병합 영역: V8-AF8
        CellRangeAddress mergedRegion3 = new CellRangeAddress(7, 7, 24, 33);
        Cell titleCell3 = titleRow.createCell(24);
        titleCell3.setCellValue("재무 및 구매 정보");
        CellStyle style3 = sheet.getWorkbook().createCellStyle();
        setTitleCellStyle(sheet, style3);  // 스타일 설정
        titleCell3.setCellStyle(style3);
        sheet.addMergedRegion(mergedRegion3);
        applyBordersToMergedRegion(sheet, mergedRegion3);
    }

    private void setTitleCellStyle(Sheet sheet, CellStyle titleStyle) {
        // 폰트 설정
        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setBold(true); // 글씨를 굵게
        titleFont.setFontHeightInPoints((short) 9); // 폰트 크기 9
        titleFont.setFontName("맑은 고딕"); // 폰트 "맑은 고딕"
        titleStyle.setFont(titleFont);

        // 배경 색상 설정 (RGB 217, 217, 217)
        XSSFColor backgroundColor = new XSSFColor(new java.awt.Color(217, 217, 217), null);
        ((XSSFCellStyle) titleStyle).setFillForegroundColor(backgroundColor);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 배경색 채우기

        // 정렬 설정
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    private void applyBordersToMergedRegion(Sheet sheet, CellRangeAddress region) {
        // 병합된 셀에 테두리 적용
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
    }

    private void setTitleRow(Sheet sheet) {
        // 두 번째 제목 행 처리
        Row titleRow1 = sheet.getRow(1);
        if (titleRow1 == null) {
            titleRow1 = sheet.createRow(1);  // 1행이 없으면 생성
        }

        // D2-R5 병합
        sheet.addMergedRegion(new CellRangeAddress(1, 4, 3, 17)); // D2-R5 병합

        // 병합된 첫 번째 셀(D2)에 텍스트 입력
        Cell titleCell3 = titleRow1.getCell(3); // 3번째 셀 참조
        if (titleCell3 == null) {
            titleCell3 = titleRow1.createCell(3); // 3번째 셀이 없으면 생성
        }

        // 텍스트 설정
        titleCell3.setCellValue("정보자산 목록 및 중요성 평가");

        // 셀 스타일 적용
        CellStyle titleCellStyle = createTitleStyle(sheet);
        titleCell3.setCellStyle(titleCellStyle);  // 스타일 적용

        // 디버깅을 위해 텍스트 출력
        System.out.println("뭐가 뜨는데: " + titleCell3.getStringCellValue());

        // 셀의 배경 색상 확인을 위한 디버깅
        System.out.println("Cell Background Color: " + titleCell3.getCellStyle().getFillForegroundColorColor());

        // 열 너비 조정
        titleRow1.setHeightInPoints(30);  // 병합된 행 높이 조정
        for (int i = 3; i <= 17; i++) {
            sheet.setColumnWidth(i, 4000);  // 병합된 열 너비 조정
        }
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

        // 테두리 설정
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        cell.setCellStyle(style);
        // 기본 행 높이 설정 (엑셀 기준 30포인트)
        row.setHeightInPoints(30);
    }


    private void addImportanceGradeInfo(Sheet sheet) {
        // 정보자산 중요성 등급 설정
        Row row2 = sheet.createRow(1);
        Cell cellS2 = row2.createCell(20);
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
                Cell cell = row.createCell(20 + j); // S 열은 18번째, T 열은 19번째
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
                    .assetUser(assetUserFullname)
                    .assetOwner(assetOwnerFullname)
                    .assetSecurityManager(assetSecurityManagerFullname)
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
                        dto.setRackUnit(itSystemEquipment.getRackUnit());
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

}
