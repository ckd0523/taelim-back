package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                Document document = assetDto.toDocument();
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
}
