package com.codehows.taelim.service;

import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.CommonAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final CommonAssetRepository commonAssetRepository;

    public void assetRegister(AssetDto assetDto){
        CommonAsset commonAsset = assetDto.toEntity();
        commonAssetRepository.save(commonAsset);
    }

}
