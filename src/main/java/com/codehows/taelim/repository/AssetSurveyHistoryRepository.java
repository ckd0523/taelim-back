package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AssetSurveyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssetSurveyHistoryRepository extends JpaRepository<AssetSurveyHistory, Long>, AssetSurveyHistoryRepositoryCustom {
    //이것보다 복잡한 쿼리가 필요해서 쿼리 dsl로 넘김
    /*@Query(value = "select * from asset_Survey_History", nativeQuery = true)
    Optional<List<AssetSurveyHistory>> findAssetSurveyHistory();*/

    //얘는 왜 또 그냥 되는건데 ㅡㅡ
    //assetSurveyDetailRepository 참고
    void deleteByAssetSurveyNoIn(List<Long> assetSurveyNo);

    //assetService에서 자산 조사 상세 이력 가져올 때 사용
    AssetSurveyHistory findByAssetSurveyNo(Long assetSurveyNo);
}
