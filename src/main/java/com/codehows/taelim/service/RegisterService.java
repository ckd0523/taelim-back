package com.codehows.taelim.service;

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

        commonAssetRepository.save(commonAsset);
        informationProtectionSystem.setAssetNo(commonAsset);
        informationProtectionSystemRepository.save(informationProtectionSystem);


    }

}
