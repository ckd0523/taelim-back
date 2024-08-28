package com.codehows.taelim.repository;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.QCommonAsset;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommonAssetRepositoryCustomImpl implements CommonAssetRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    //자산코드로 하나의 자산 공통정보 가져오기
    @Override
    public Optional<CommonAsset> findLatestApprovedAsset(String assetCode) {
        CommonAsset result = queryFactory
                .selectFrom(QCommonAsset.commonAsset)  // QCommonAsset 사용
                .where(QCommonAsset.commonAsset.assetCode.eq(assetCode)
                        .and(QCommonAsset.commonAsset.disposalStatus.eq(false))
                        .and(QCommonAsset.commonAsset.approval.eq(Approval.APPROVE)))
                .orderBy(QCommonAsset.commonAsset.assetNo.desc()) // 최신 데이터부터 정렬
                .fetchFirst(); // 가장 최신의 하나만 가져옴

        return Optional.ofNullable(result);
    }

    // 자산목록 (자산 공통정보)
    @Override
    public List<CommonAsset> findApprovedAndNotDisposedAssets() {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 서브쿼리: 각 자산 코드별 최대 자산 번호 찾기
        JPAQuery<Long> subQuery = new JPAQuery<Long>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset;

        subQuery.select(subCa.assetNo.max())
                .from(subCa)
                .where(subCa.approval.eq(Approval.valueOf("APPROVE"))
                        .and(subCa.disposalStatus.isFalse()))
                .groupBy(subCa.assetCode);

        // 메인 쿼리: 최신 자산 정보 조회
        JPAQuery<CommonAsset> query = new JPAQuery<CommonAsset>(entityManager);

        return query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery))
                .fetch();
    }

    @Override
    public List<CommonAsset> findAssetNoByAssetLocation(AssetLocation location) {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 서브쿼리: 각 자산 코드별 최대 자산 번호 찾기
        JPAQuery<Long> subQuery = new JPAQuery<Long>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset;

        subQuery.select(subCa.assetNo.max())
                .from(subCa)
                .where(subCa.approval.eq(Approval.valueOf("APPROVE"))
                        .and(subCa.disposalStatus.isFalse())
                        .and(subCa.assetLocation.eq(location))
                )
                .groupBy(subCa.assetCode);

        // 메인 쿼리: 최신 자산 정보 조회
        JPAQuery<CommonAsset> query = new JPAQuery<CommonAsset>(entityManager);

        return query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery))
                .fetch();
    }
}
