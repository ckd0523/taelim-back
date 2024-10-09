package com.codehows.taelim.repository;

import com.codehows.taelim.dto.AssetSurveyUpdateDto;
import com.codehows.taelim.entity.AssetSurveyDetail;
import com.codehows.taelim.entity.AssetSurveyHistory;
import java.util.List;

public interface AssetSurveyDetailRepositoryCustom {

    void updateAssetSurveyDetail(AssetSurveyUpdateDto updateDto);

    void updateAssetSurveyDetail2(AssetSurveyUpdateDto updateDto);

    // 상세정보에서 자산조사 이력을 들고오기위한 쿼리 작성 Impl 에서
    List<AssetSurveyDetail> findByAssetCode(String assetCode);
}
