package com.codehows.taelim.service;

import com.codehows.taelim.dto.AllUpdateDto;
import com.codehows.taelim.dto.AssetUpdateDto;
import com.codehows.taelim.entity.*;
import com.codehows.taelim.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.codehows.taelim.constant.AssetClassification.INFORMATION_PROTECTION_SYSTEM;

@RequiredArgsConstructor
@Service
public class UpdateService {
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

    @PersistenceContext
    private EntityManager entityManager;

    public void update(AssetUpdateDto updateDto) {

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
                .confidentiality(updateDto.getConfidentiality())
                .integrity(updateDto.getIntegrity())
                .availability(updateDto.getAvailability())
                .note(updateDto.getNote())
                .manufacturingCompany(updateDto.getManufacturingCompany())
                .ownership(updateDto.getOwnership())
                .purchaseCost(updateDto.getPurchaseCost())
                .purchaseDate(updateDto.getPurchaseDate())
                .usefulLife(updateDto.getUsefulLife())
                .depreciationMethod(updateDto.getDepreciationMethod())
                .warrantyDetails(updateDto.getWarrantyDetails())
                .attachment(updateDto.getAttachment())
                .purchaseSource(updateDto.getPurchaseSource())
                .contactInformation(updateDto.getContactInformation())
                .disposalStatus(updateDto.getDisposalStatus())
                .demandStatus(updateDto.getDemandStatus())
                .approval(updateDto.getApproval())
                .createDate(updateDto.getCreateDate())
                .build();

        commonAssetRepository.save(commonAsset);
    }

    public Optional<CommonAsset> findById(Long id) {
        return commonAssetRepository.findById(id);
    }
    public List<CommonAsset> findAll() {
        return commonAssetRepository.findAll();
    }

    public String allUpdate(AllUpdateDto allUpdateDto) {
        // Optional 처리
        CommonAsset commonAsset = commonAssetRepository.findById(allUpdateDto.getAssetNo())
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
        commonAsset.setAssetNo(null);
        CommonAsset commonAsset1 = commonAssetRepository.save(commonAsset); // 영속 상태로 저장

        // AssetClassification에 따른 처리
        switch (commonAsset.getAssetClassification()) {
            case SOFTWARE -> {
                Software software = softwareRepository.findByAssetNo(commonAsset);

                    software.setSoftwareNo(null);
                    software.setAssetNo(commonAsset1);
                    softwareRepository.save(software);

            }
            case CAR -> {
                Car car = carRepository.findByAssetNo(commonAsset);

                    car.setCarNo(null);
                    car.setAssetNo(commonAsset1);
                    carRepository.save(car);

            }
        }

        return "good";
    }
}
