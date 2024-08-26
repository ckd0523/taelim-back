package com.codehows.taelim.repository;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.CommonAsset;

import java.util.List;
import java.util.Optional;


public interface CommonAssetRepositoryCustom {
    Optional<CommonAsset> findLatestApprovedAsset(String assetCode);

    List<CommonAsset> findApprovedAndNotDisposedAssets();

    List<CommonAsset> findAssetNoByAssetLocation(AssetLocation location);
}
