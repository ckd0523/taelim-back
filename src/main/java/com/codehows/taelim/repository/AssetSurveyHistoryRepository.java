package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AssetSurveyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssetSurveyHistoryRepository extends JpaRepository<AssetSurveyHistory, Long>, AssetSurveyHistoryRepositoryCustom {
    /*@Query(value = "select * from asset_Survey_History", nativeQuery = true)
    Optional<List<AssetSurveyHistory>> findAssetSurveyHistory();*/
}
