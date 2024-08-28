package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AssetSurveyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetSurveyDetailRepository extends JpaRepository<AssetSurveyDetail, Long> {
    void deleteByAssetSurveyNoIn(List<Long> assetSurveyNo);
}
