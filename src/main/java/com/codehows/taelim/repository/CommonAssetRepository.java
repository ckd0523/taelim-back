package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonAssetRepository extends JpaRepository<CommonAsset, Long>, CommonAssetRepositoryCustom {

    CommonAsset findTopByOrderByAssetNoDesc();
}
