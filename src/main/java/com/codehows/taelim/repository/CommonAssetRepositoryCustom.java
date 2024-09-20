package com.codehows.taelim.repository;

import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.CommonAsset;

import java.util.List;
import java.util.Optional;


public interface CommonAssetRepositoryCustom {
    Optional<CommonAsset> findLatestApprovedAsset(String assetCode);

    // 자산 수정할때 하나의 assetCode 중에 데이터가 여러개일때 최신 assetNo 조회 하는 쿼리
    Optional<CommonAsset> findLatestAssetCode(String assetCode);

    // 자산목록 (자산 공통정보) - 조건 : 폐기여부 F and 폐기여부 T + 요청 미확인 and 폐기여부 T + 요청 거절   리스트 조회
    List<CommonAsset> findApprovedAndNotDisposedAssets();

    // 새로운 메서드: 자산 분류에 따른 최신 자산 코드 가져오기
    Optional<String> findLastAssetCodeByClassification(AssetClassification classification);
    List<CommonAsset> findAssetNoByAssetLocation(AssetLocation location);
    // 수정 이력
    List<CommonAsset> findApprovedAssetsByCodeExceptLatest(String assetCode);

    //위치에 따른 자산 목록
    List<CommonAsset> findDetailByLocation(AssetLocation location);
}
