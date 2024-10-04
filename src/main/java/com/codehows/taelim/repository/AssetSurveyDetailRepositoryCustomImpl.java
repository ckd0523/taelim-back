package com.codehows.taelim.repository;

import com.codehows.taelim.dto.AssetSurveyUpdateDto;
import com.codehows.taelim.entity.QAssetSurveyDetail;
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
    public void updateAssetSurveyDetail(AssetSurveyUpdateDto updateDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAssetSurveyDetail assetSurveyDetail = QAssetSurveyDetail.assetSurveyDetail;

        // requestType에 따라 로직 분기
        if (updateDto.isRequestType()) {
            // 정위치 유무가 true일 경우의 처리
            queryFactory.update(assetSurveyDetail)
                    .set(assetSurveyDetail.exactLocation, updateDto.isUpdateValue())
                    .where(assetSurveyDetail.infoNo.eq(updateDto.getInfoNo()))
                    .execute();
        } else {
            // 상태가 false일 경우의 처리
            queryFactory.update(assetSurveyDetail)
                    .set(assetSurveyDetail.assetStatus, updateDto.isUpdateValue())
                    .where(assetSurveyDetail.infoNo.eq(updateDto.getInfoNo()))
                    .execute();
        }
    }

    @Override
    public void updateAssetSurveyDetail2(AssetSurveyUpdateDto updateDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAssetSurveyDetail assetSurveyDetail = QAssetSurveyDetail.assetSurveyDetail;

        queryFactory.update(assetSurveyDetail)
                .set(assetSurveyDetail.assetSurveyContent, updateDto.getContent())
                .where(assetSurveyDetail.infoNo.eq(updateDto.getInfoNo()))
                .execute();
    }

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
