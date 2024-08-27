package com.codehows.taelim.service;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.InformationProtectionSystem;
import com.codehows.taelim.entity.Member;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.InformationProtectionSystemRepository;
import com.codehows.taelim.repository.MemberRepository;
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
    private final InformationProtectionSystemRepository informationProtectionSystemRepository;

    private final MemberRepository memberRepository;
    public void assetRegister(AssetDto assetDto){

        Member assetUser = memberRepository.findByEmail(assetDto.getAssetUser());
        Member assetOwner = memberRepository.findByEmail(assetDto.getAssetOwner());
        Member assetSecurityManager = memberRepository.findByEmail(assetDto.getAssetSecurityManager());
        CommonAsset commonAsset = assetDto.toEntity();
        InformationProtectionSystem informationProtectionSystem = assetDto.toEntity2();
        commonAsset.setAssetUser(assetUser);
        commonAsset.setAssetOwner(assetOwner);
        commonAsset.setAssetSecurityManager(assetSecurityManager);
        commonAsset.setApproval(Approval.APPROVE);
        commonAsset.setDisposalStatus(Boolean.FALSE);

        commonAssetRepository.save(commonAsset);
        informationProtectionSystem.setAssetNo(commonAsset);
        informationProtectionSystemRepository.save(informationProtectionSystem);


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
