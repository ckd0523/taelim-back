package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonAssetRepository extends JpaRepository<CommonAsset, Long>, CommonAssetRepositoryCustom {

    CommonAsset findTopByOrderByAssetNoDesc();

    Optional<CommonAsset> findByAssetCode(String assetCode);

}
