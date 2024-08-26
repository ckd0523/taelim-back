package com.codehows.taelim.service;

import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Software;
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

    //자산코드로 하나의 자산 공통정보 가져오기
    public Optional<CommonAsset> getCommonAsset(String assetCode) {
      return commonAssetRepository.findLatestApprovedAsset(assetCode);
    }

    // 자산목록 (자산 공통정보)
    public List<CommonAsset> getApprovedAndNotDisposedAssets() {
        return commonAssetRepository.findApprovedAndNotDisposedAssets();
    }

    public AssetDto getAssetDetail(String assetCode) {

        AssetDto assetDto = new AssetDto();
        Optional<CommonAsset> commonAssetObj = commonAssetRepository.findLatestApprovedAsset(assetCode);
        CommonAsset commonAsset = commonAssetObj.get();
        assetDto.setAssetNo(commonAsset.getAssetNo());
        assetDto.setAssetBasis(commonAsset.getAssetBasis());
        assetDto.setAssetCode(commonAsset.getAssetCode());
        AssetClassification AssetClassification = commonAsset.getAssetClassification();
        switch (AssetClassification) {
            case SOFTWARE -> {
                Software software = softwareRepository.findByAssetNo(commonAsset);
                assetDto.setCompanyManager(software.getCompanyManager());
                assetDto.setIP(software.getIP());
                assetDto.setOS(software.getOS());
                assetDto.setServerId(software.getServerId());
                assetDto.setServerPassword(software.getServerPassword());
                return assetDto;
            }

        }
        return assetDto;
    }
}
