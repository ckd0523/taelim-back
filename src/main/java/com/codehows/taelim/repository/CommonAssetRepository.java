package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommonAssetRepository extends JpaRepository<CommonAsset, Long>, CommonAssetRepositoryCustom {

    CommonAsset findTopByOrderByAssetNoDesc();

    Optional<CommonAsset> findByAssetCode(String assetCode);

    // 최신 자산과 그 이전 자산 가져오는 쿼리
    @Query("SELECT a FROM CommonAsset a WHERE a.assetCode = :assetCode ORDER BY a.assetNo DESC")
    List<CommonAsset> findTop2ByAssetCodeOrderByAssetNoDesc(@Param("assetCode") String assetCode);
}
