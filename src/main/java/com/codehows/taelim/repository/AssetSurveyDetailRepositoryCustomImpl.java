package com.codehows.taelim.repository;

import com.codehows.taelim.dto.AssetSurveyUpdateDto;
import com.codehows.taelim.entity.QAssetSurveyDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
