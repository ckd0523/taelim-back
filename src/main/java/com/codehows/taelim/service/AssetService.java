package com.codehows.taelim.service;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.CommonAssetRepository;
import com.codehows.taelim.repository.SoftwareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AssetService {

    private final CommonAssetRepository commonAssetRepository;
    private final SoftwareRepository softwareRepository;

    //자산조회
    public Optional<CommonAsset> getCommonAsset(String assetCode) {
      return commonAssetRepository.findLatestApprovedAsset(assetCode);
    }

    public List<CommonAsset> getApprovedAndNotDisposedAssets() {
        return commonAssetRepository.findApprovedAndNotDisposedAssets();
    }
}
