package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AssetSurveyDetail;
import com.codehows.taelim.entity.AssetSurveyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetSurveyDetailRepository extends JpaRepository<AssetSurveyDetail, Long> {

    //gpt가 List<Long>으로 받아 처리하려면 jpa 형식 메서드 뒤에 In을 붙이면 된다고 했는데
    //잘 안되서 네이티브 쿼리로 해결
    //또 다른 이유는 deleteAllByAssetSuveryNo를 jpa로 처리할 시
    //jpa에서는 select문을 실행 후 delete 쿼리 실행
    //이는 각각에 대해 수행되기 때문에 대량의 데이터를 삭제할 때 불필요한 쿼리가 많이 발생 -> 성능 저하
    //하지만 이 시스템에서는 삭제 쿼리가 많이 발생하지 않고 삭제할 데이터가 많지 않음
    //그럼에도 네이티브 쿼리를 사용하는 이유는 잘 안되서
    //@Param으로 List에 있는 값을 In으로 하나씩 넘겨주며 처리하면
    //단일 쿼리로 삭제를 수행할 수 있다.
    //추가로 이렇게 Jpa에서 제공하는 기본 crud를 사용하지 않고 직접 쿼리를 작성하거나 네이티브 쿼리를 사용하면
    //@Modifying 어노테이션을 붙여야함
    @Modifying
    @Query(value = "delete from asset_survey_detail where asset_survey_no in :asset_Survey_No", nativeQuery = true)
    void deleteByAssetSurveyNoIn(@Param("asset_Survey_No") List<Long> asset_Survey_No);

    //자산 조사를 위한 자산 조사 상세 이력
    List<AssetSurveyDetail> findAllByAssetSurveyNo(AssetSurveyHistory assetSurveyHistory);
}
