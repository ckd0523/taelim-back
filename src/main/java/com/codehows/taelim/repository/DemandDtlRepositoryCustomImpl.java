package com.codehows.taelim.repository;


import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.dto.DeleteHistoryDto;
import com.codehows.taelim.entity.DemandDtl;
import com.codehows.taelim.entity.QCommonAsset;
import com.codehows.taelim.entity.QDemand;
import com.codehows.taelim.entity.QDemandDtl;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class DemandDtlRepositoryCustomImpl implements DemandDtlRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DemandDtl> findDeleteHistory() {
        QDemandDtl demandDtl = QDemandDtl.demandDtl;
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        QDemand demand = QDemand.demand;
        // 서브 쿼리
        JPAQuery<DemandDtl> subQuery = new JPAQuery<>(entityManager);

       return subQuery.select(demandDtl)
               .from(demandDtl)
               .join(commonAsset).on(demandDtl.assetNo.assetNo.eq(commonAsset.assetNo))
               .join(demand).on(demandDtl.demandNo.demandNo.eq(demand.demandNo))
               .where(
                       commonAsset.disposalStatus.isTrue()
                               .and(commonAsset.approval.eq(Approval.APPROVE))
               )
               .fetch();
    }

    @Override
    public List<DemandDtl> findUpdateHistory() {
        QDemandDtl demandDtl = QDemandDtl.demandDtl;
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        QDemand demand = QDemand.demand;

        // 서브쿼리
        JPAQuery<DemandDtl> subQuery = new JPAQuery<>(entityManager);

        return subQuery.select(demandDtl)
                .from(demandDtl)
                .join(commonAsset).on(demandDtl.assetNo.assetNo.eq(commonAsset.assetNo))
                .join(demand).on(demandDtl.demandNo.demandNo.eq(demand.demandNo))
                .where(
                        commonAsset.disposalStatus.isFalse()
                                .and(commonAsset.approval.eq(Approval.APPROVE))

                )
                .fetch();
    }

    // 상세정보화면에서 수정이력을 가져오는 쿼리
    @Override
    public List<DemandDtl> findUpdateHistoryByAssetCode(String assetCode) {
        QDemandDtl demandDtl = QDemandDtl.demandDtl;
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        QDemand demand = QDemand.demand;

        // 서브쿼리
        JPAQuery<DemandDtl> subQuery = new JPAQuery<>(entityManager);

        return subQuery.select(demandDtl)
                .from(demandDtl)
                .join(commonAsset).on(demandDtl.assetNo.assetNo.eq(commonAsset.assetNo))
                .join(demand).on(demandDtl.demandNo.demandNo.eq(demand.demandNo))
                .where(
                        commonAsset.assetCode.eq(assetCode) // 특정 assetCode 필터링
                                .and(commonAsset.disposalStatus.isFalse())
                                .and(commonAsset.approval.eq(Approval.APPROVE))
                )
                .fetch();
    }
}
