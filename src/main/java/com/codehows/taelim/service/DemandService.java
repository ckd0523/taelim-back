package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.dto.DemandHistoryAllDto;
import com.codehows.taelim.dto.DemandHistoryDto;
import com.codehows.taelim.dto.UnconfirmedDemandDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DemandService {

    private final DemandDtlRepository demandDtlRepository;
    private final CommonAssetRepository commonAssetRepository;
    private final DemandRepository demandRepository;
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


    public List<DemandHistoryDto> getAllDemandHistory() {

        List<DemandDtl> demandDtls = demandDtlRepository.findAllByOrderByDemandDtlNoDesc();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()){
                Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                Demand demand1 = demand.orElseThrow();
                if(demand1.getDisposeLocation() == null){
                    //수정이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("update");
                }else {
                    //폐기이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("delete");
                    demandHistoryDto.setDisposeLocation(demand1.getDisposeLocation());
                    demandHistoryDto.setDisposeMethod(demand1.getDisposeMethod());
                }
                demandHistoryDto.setAssetName(asset.getAssetName());
                demandHistoryDto.setDemandReason(demand1.getDemandReason());
                demandHistoryDto.setDemandDetail(demand1.getDemandDetail());
                demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                demandHistoryDto.setAssetNo(asset.getAssetNo());
                demandHistoryDto.setAssetCode(asset.getAssetCode());
                demandHistoryDto.setDemandDate(asset.getCreateDate());
                demandHistoryDto.setDemandStatus(asset.getApproval().toString());

                demandHistoryDtos.add(demandHistoryDto);
            }

        }

        return demandHistoryDtos;

    }

    public List<DemandHistoryDto> getAssetDemandHistory(String assetCode) {


        List<DemandDtl> demandDtls = demandDtlRepository.findUpdateHistoryByAssetCode(assetCode);

        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()){
                Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                Demand demand1 = demand.orElseThrow();
                if(demand1.getDisposeLocation() == null){
                    //수정이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("update");
                }else {
                    //폐기이력
                    demandHistoryDto.setDemandBy("이창현");
                    demandHistoryDto.setDemandType("delete");
                }
                demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                demandHistoryDto.setAssetNo(asset.getAssetNo());
                demandHistoryDto.setAssetCode(asset.getAssetCode());
                demandHistoryDto.setDemandDate(asset.getCreateDate());
                demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                demandHistoryDtos.add(demandHistoryDto);
            }

        }

        return demandHistoryDtos;

    }


    // 미확인 자산 가져오는 서비스
    public List<UnconfirmedDemandDto> getUnconfirmedDemandHistory() {

        List<DemandDtl> demandDtls = demandDtlRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();
        List<UnconfirmedDemandDto> unconfirmedDemandDtos = new ArrayList<>();
        for(DemandDtl demandDtl : demandDtls){
            Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
            CommonAsset asset = commonAsset.orElseThrow();
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();
            if(asset.getDemandStatus()) {
                if (asset.getApproval() == Approval.UNCONFIRMED) {
                    Optional<Demand> demand = demandRepository.findById(demandDtl.getDemandNo().getDemandNo());
                    Demand demand1 = demand.orElseThrow();
                    if (demand1.getDisposeLocation() == null) {
                        //수정이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("update");
                    } else {
                        //폐기이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("delete");
                    }
                    demandHistoryDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                    demandHistoryDto.setAssetNo(asset.getAssetNo());
                    demandHistoryDto.setAssetCode(asset.getAssetCode());
                    demandHistoryDto.setDemandDate(asset.getCreateDate());
                    demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                    demandHistoryDtos.add(demandHistoryDto);
                }
            }

        }

        for(DemandHistoryDto demandHistoryDto : demandHistoryDtos){
            UnconfirmedDemandDto unconfirmedDemandDto = new UnconfirmedDemandDto();
            if(Objects.equals(demandHistoryDto.getDemandType(), "update")){
                unconfirmedDemandDto.setAssetDto(getUpdateDetail(demandHistoryDto.getAssetNo()));
                unconfirmedDemandDto.setDemandHistoryDto(demandHistoryDto);
            }else {
                unconfirmedDemandDto.setDemandHistoryDto(demandHistoryDto);
            }
            unconfirmedDemandDtos.add(unconfirmedDemandDto);
        }

        return unconfirmedDemandDtos;

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
                    .usestate(asset.getUseState())
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

    public List<DemandHistoryDto> getAllDemandHistory1() {

        List<Demand> demands = demandRepository.findAll();
        List<DemandHistoryDto> demandHistoryDtos = new ArrayList<>();

        for (Demand demand : demands) {

            List<DemandDtl> demandDtls1 = demandDtlRepository.findByDemandNo_DemandNo(demand.getDemandNo());
            DemandHistoryDto demandHistoryDto = new DemandHistoryDto();

            // demandDtls1 리스트의 크기 확인
            if (demandDtls1.size() == 1) {
                DemandDtl demandDtl2 = demandDtls1.get(0);
                Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl2.getAssetNo().getAssetNo());
                CommonAsset asset = commonAsset.orElseThrow();

                if (asset.getDemandStatus()) {
                    if (demand.getDisposeLocation() == null) {
                        // 수정이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("update");
                    } else {
                        // 폐기이력
                        demandHistoryDto.setDemandBy("이창현");
                        demandHistoryDto.setDemandType("delete");
                    }
                    demandHistoryDto.setDemandNo(demandDtl2.getDemandNo().getDemandNo());
                    demandHistoryDto.setAssetNo(asset.getAssetNo());
                    demandHistoryDto.setAssetCode(asset.getAssetCode());
                    demandHistoryDto.setDemandDate(asset.getCreateDate());
                    demandHistoryDto.setDemandStatus(asset.getApproval().toString());
                    demandHistoryDtos.add(demandHistoryDto);
                }

            } else if (demandDtls1.size() > 1) {
                // 리스트가 여러 개인 경우의 처리
                List<DemandHistoryAllDto> detailAllDtos = new ArrayList<>();
                DemandHistoryDto demandHistoryDto1 = new DemandHistoryDto();
                boolean hasUnconfirmed = false;
                boolean allApprovedOrRefused = true;

                for (DemandDtl demandDtl : demandDtls1) {
                    Optional<CommonAsset> commonAsset = commonAssetRepository.findById(demandDtl.getAssetNo().getAssetNo());
                    CommonAsset asset = commonAsset.orElseThrow();
                    DemandHistoryAllDto demandHistoryAllDto = new DemandHistoryAllDto();

                    if (asset.getDemandStatus()) {
                        if (demand.getDisposeLocation() == null) {
                            // 수정이력
                            demandHistoryAllDto.setDemandBy("이창현");
                            demandHistoryAllDto.setDemandType("allUpdateDemand");
                        } else {
                            // 폐기이력
                            demandHistoryAllDto.setDemandBy("이창현");
                            demandHistoryAllDto.setDemandType("allDisposeDemand");
                        }
                        demandHistoryAllDto.setDemandNo(demandDtl.getDemandNo().getDemandNo());
                        demandHistoryAllDto.setAssetNo(asset.getAssetNo());
                        demandHistoryAllDto.setAssetCode(asset.getAssetCode());
                        demandHistoryAllDto.setDemandDate(asset.getCreateDate());
                        demandHistoryAllDto.setDemandStatus(asset.getApproval().toString());
                        detailAllDtos.add(demandHistoryAllDto);

                        // 상태 확인 로직
                        if (asset.getApproval() == Approval.UNCONFIRMED) {
                            hasUnconfirmed = true;
                        } else if (asset.getApproval() != Approval.APPROVE && asset.getApproval() != Approval.REFUSAL) {
                            allApprovedOrRefused = false;
                        }
                    }
                }

                // 상태 결정 로직
                if (hasUnconfirmed) {
                    demandHistoryDto1.setDemandStatus("UNCONFIRMED");
                } else if (allApprovedOrRefused) {
                    demandHistoryDto1.setDemandStatus("completed");
                } else {
                    demandHistoryDto1.setDemandStatus("UNCONFIRMED");
                }
                if (demand.getDisposeLocation() == null) {
                    demandHistoryDto1.setDemandType("allUpdateDemand");
                } else {
                    demandHistoryDto1.setDemandType("allDisposeDemand");
                }
                demandHistoryDto1.setSubRows(detailAllDtos);
                demandHistoryDto1.setDemandDate(demand.getDemandDate());
                demandHistoryDto1.setDemandBy("이창현");
                demandHistoryDtos.add(demandHistoryDto1);
            }
        }

        return demandHistoryDtos;
    }

}
