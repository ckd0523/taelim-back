package com.codehows.taelim.repository;

import com.codehows.taelim.dto.AssetSurveyHistoryDto;

import java.util.List;
import java.util.Optional;

public interface AssetSurveyHistoryRepositoryCustom {
    List<AssetSurveyHistoryDto> findAssetSurveyHistoryAndMemberName();
}
