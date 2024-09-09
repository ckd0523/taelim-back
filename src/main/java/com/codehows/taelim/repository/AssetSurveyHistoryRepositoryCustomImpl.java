package com.codehows.taelim.repository;

import com.codehows.taelim.dto.AssetSurveyHistoryDto;
import com.codehows.taelim.entity.AssetSurveyHistory;
import com.codehows.taelim.entity.QAssetSurveyHistory;
import com.codehows.taelim.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AssetSurveyHistoryRepositoryCustomImpl implements AssetSurveyHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<AssetSurveyHistoryDto> findAssetSurveyHistoryAndMemberName() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QAssetSurveyHistory assetSurveyHistory = QAssetSurveyHistory.assetSurveyHistory;
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(
                        AssetSurveyHistoryDto.class,
                        assetSurveyHistory.assetSurveyNo, // asset_survey_no
                        member.uName, // member.u_name
                        assetSurveyHistory.assetSurveyLocation, // asset_survey_location
                        assetSurveyHistory.round, // ROUND
                        assetSurveyHistory.surveyStatus, // survey_status
                        assetSurveyHistory.assetSurveyStartDate, // asset_survey_start_date
                        assetSurveyHistory.assetSurveyEndDate // asset_survey_end_date
                ))
                .from(assetSurveyHistory)
                .join(assetSurveyHistory.assetSurveyBy, member) // member.email = asset_survey_history.asset_survey_by
                .fetch();
    }
}
