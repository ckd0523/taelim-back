package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.OtherAssets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtherAssetsRepository extends JpaRepository<OtherAssets, Long> {
    OtherAssets findByAssetNo(CommonAsset commonAsset);
}
