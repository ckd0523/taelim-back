package com.codehows.taelim.service;

import com.codehows.taelim.dto.UpdateDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class updateService {
    private final CommonAssetRepository commonAssetRepository;
    private final InformationProtectionSystemRepository informationProtectionSystemRepository;
    private final ApplicationProgramRepository applicationProgramRepository;
    private final SoftwareRepository softwareRepository;
    private final ElectronicInformationRepository electronicInformationRepository;
    private final DocumentRepository documentRepository;
    private final PatentsAndTrademarksRepository patentsAndTrademarksRepository;
    private final ItSystemEquipmentRepository itSystemEquipmentRepository;
    private final ItNetworkEquipmentRepository itNetworkEquipmentRepository;
    private final TerminalRepository terminalRepository;
    private final DevicesRepository devicesRepository;
    private final CarRepository carRepository;
    private final FurnitureRepository furnitureRepository;
    private final OtherAssetsRepository otherAssetsRepository;

    public void update(UpdateDto updateDto) {

        CommonAsset commonAsset = CommonAsset.builder()
                .assetNo(updateDto.getAssetNo())
                .assetUser(updateDto.getAssetUser())
                .assetOwner(updateDto.getAssetOwner())
                .assetSecurityManager(updateDto.getAssetSecurityManager())
                .assetClassification(updateDto.getAssetClassification())
                .assetBasis(updateDto.getAssetBasis())
                .assetCode(updateDto.getAssetCode())
                .assetName(updateDto.getAssetName())
                .purpose(updateDto.getPurpose())
                .quantity(updateDto.getQuantity())
                .department(updateDto.getDepartment())
                .assetLocation(updateDto.getAssetLocation())
                .operationStatus(updateDto.getOperationStatus())
                .introducedDate(updateDto.getIntroducedDate())
                .build();

        commonAssetRepository.save(commonAsset);
    }
}
