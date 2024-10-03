package com.codehows.taelim.repository;

import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.AssetSurveyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    //위치로 자산 조사 이력 가져오기
    AssetSurveyHistory findByAssetSurveyLocation(AssetLocation location);

    //자산 조사 등록 시 해당 위치에 대해 진행 중인 조사가 있는지 확인
    //AssetSurveyHistory findTop1ByAssetLocationOrderByAssetSurveyNoDesc(AssetLocation location);

    List<AssetSurveyHistory> findByAssetSurveyNoIn(List<Long> assetSurveyNos);

    @Modifying
    @Transactional
    @Query("UPDATE AssetSurveyHistory a SET a.surveyStatus = true, a.assetSurveyEndDate = :endDate WHERE a.assetSurveyNo = :assetSurveyNo")
    void completeSurvey(@Param("assetSurveyNo") Long assetSurveyNo, @Param("endDate") LocalDate endDate);
}
