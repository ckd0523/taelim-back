package com.codehows.taelim.repository;

import com.codehows.taelim.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AssetSurveyDetailRepositoryCustomImpl implements AssetSurveyDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AssetSurveyDetail> findByAssetCode(String assetCode) {
        QAssetSurveyDetail assetSurveyDetail = QAssetSurveyDetail.assetSurveyDetail;
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        QAssetSurveyHistory assetSurveyHistory = QAssetSurveyHistory.assetSurveyHistory;

        // 서브쿼리 작성
        JPAQuery<AssetSurveyDetail> subQuery = new JPAQuery<>(entityManager);

        // 관련 쿼리
        return subQuery.select(assetSurveyDetail)
                .from(assetSurveyDetail)
                .join(commonAsset).on(assetSurveyDetail.assetNo.assetNo.eq(commonAsset.assetNo))
                .join(assetSurveyHistory).on(assetSurveyHistory.assetSurveyNo.eq(assetSurveyHistory.assetSurveyNo))
                .where(commonAsset.assetCode.eq(assetCode))
                .fetch();
    }
}
