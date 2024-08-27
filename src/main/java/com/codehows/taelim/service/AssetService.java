package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    //자산코드로 하나의 자산 공통정보 가져오기
    public Optional<CommonAsset> getCommonAsset(String assetCode) {
      return commonAssetRepository.findLatestApprovedAsset(assetCode);
    }

    // 자산목록 (자산 공통정보)
    public List<CommonAsset> getApprovedAndNotDisposedAssets() {
        return commonAssetRepository.findApprovedAndNotDisposedAssets();
    }

    // 자산 상세 조회
    public AssetDto getAssetDetail(String assetCode) {

        AssetDto assetDto = new AssetDto();
        Optional<CommonAsset> commonAssetObj = commonAssetRepository.findLatestApprovedAsset(assetCode);
        CommonAsset commonAsset = commonAssetObj.get();
        assetDto.setAssetNo(commonAsset.getAssetNo());
        assetDto.setAssetBasis(commonAsset.getAssetBasis());
        assetDto.setAssetCode(commonAsset.getAssetCode());
        assetDto.setAssetClassification(commonAsset.getAssetClassification());
        assetDto.setAssetName(commonAsset.getAssetName());
        assetDto.setManufacturingCompany(commonAsset.getManufacturingCompany());
        assetDto.setPurpose(commonAsset.getPurpose());
        assetDto.setDepartment(commonAsset.getDepartment());
        assetDto.setAssetLocation(commonAsset.getAssetLocation());
        assetDto.setUseState(commonAsset.getUseState());
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
        assetDto.setQRInformation(commonAsset.getQRInformation());
        assetDto.setDisposalStatus(commonAsset.getDisposalStatus());
        //assetDto.setDemandStatus(commonAsset.getDemandStatus);
        assetDto.setApproval(commonAsset.getApproval());
        assetDto.setDemandCheck(commonAsset.getDemandCheck());
        assetDto.setCreateDate(commonAsset.getCreateDate());
        AssetClassification AssetClassification = commonAsset.getAssetClassification();
        switch (AssetClassification) {
            case SOFTWARE -> {
                Software software = softwareRepository.findByAssetNo(commonAsset);
                assetDto.setCompanyManager(software.getCompanyManager());
                assetDto.setIP(software.getIP());
                assetDto.setOS(software.getOS());
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
                assetDto.setIP(terminal.getIP());
                assetDto.setProductSerialNumber(terminal.getProductSerialNumber());
                assetDto.setOS(terminal.getOS());
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
                assetDto.setOS(applicationProgram.getOS());
                assetDto.setRelatedDB(applicationProgram.getRelatedDB());
                assetDto.setIP(applicationProgram.getIP());
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
                assetDto.setOS(electronicInformation.getOS());
                assetDto.setSystem(electronicInformation.getSystem());
                assetDto.setDBType(electronicInformation.getDBType());
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
                assetDto.setRelatedDocuments(patentsAndTrademarks.getRelatedDocuments());
            }
            case INFORMATION_PROTECTION_SYSTEM -> {
                InformationProtectionSystem informationProtectionSystem = informationProtectionSystemRepository.findByAssetNo(commonAsset);
                assetDto.setServiceScope(informationProtectionSystem.getServiceScope());
            }

        }
        return assetDto;
    }

    //폐기 승인 처리
    public CommonAsset DisposeApprove(String assetCode){

        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.TRUE);
        commonAssetRepository.save(commonAsset);
        return commonAsset;

    }

    // 폐기 거절 처리
    public CommonAsset DisposeRefusal(String assetCode, String Comment){
        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
        commonAsset.setApproval(Approval.REFUSAL);
        commonAssetRepository.save(commonAsset);
        //코멘트 들어가야함

        return commonAsset;
    }

    //수정 승인 처리
    public CommonAsset UpdateApprove(String assetCode, String Comment){
        CommonAsset commonAsset = commonAssetRepository.findLatestApprovedAsset(assetCode).get();
        commonAsset.setApproval(Approval.APPROVE);
        commonAssetRepository.save(commonAsset);
        //코멘트 들어가야함
        return commonAsset;
    }

}
