package com.codehows.taelim.repository;

import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommonAssetRepository extends JpaRepository<CommonAsset, Long>, CommonAssetRepositoryCustom {

    CommonAsset findTopByOrderByAssetNoDesc();

    Optional<CommonAsset> findByAssetCode(String assetCode);


    // 분류에 맞느 자산을 필터링해서 가져오는 메서드
    //List<CommonAsset> findByClassification(AssetClassification assetClassification);

}
