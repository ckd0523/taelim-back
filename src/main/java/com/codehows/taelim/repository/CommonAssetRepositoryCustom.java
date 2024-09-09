package com.codehows.taelim.repository;

import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.CommonAsset;

import java.util.List;
import java.util.Optional;


public interface CommonAssetRepositoryCustom {
    Optional<CommonAsset> findLatestApprovedAsset(String assetCode);

    List<CommonAsset> findApprovedAndNotDisposedAssets();

    // 새로운 메서드: 자산 분류에 따른 최신 자산 코드 가져오기
    Optional<String> findLastAssetCodeByClassification(AssetClassification classification);
    List<CommonAsset> findAssetNoByAssetLocation(AssetLocation location);
    // 수정 이력
    List<CommonAsset> findApprovedAssetsByCodeExceptLatest(String assetCode);
}
