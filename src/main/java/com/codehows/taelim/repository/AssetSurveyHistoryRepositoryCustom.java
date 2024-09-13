package com.codehows.taelim.repository;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.entity.AssetSurveyHistory;

import java.util.List;
import java.util.Optional;

public interface AssetSurveyHistoryRepositoryCustom {
    List<AssetSurveyHistoryDto> findAssetSurveyHistoryAndMemberName();

    Optional<AssetSurveyHistory> findLastAssetSurvey(AssetLocation location, boolean status);
}
