package com.codehows.taelim.repository;

import com.codehows.taelim.constant.Approval;
import com.codehows.taelim.constant.AssetClassification;
import com.codehows.taelim.constant.AssetLocation;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.QCommonAsset;
import com.querydsl.jpa.JPAExpressions;
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

    //자산코드로 하나의 자산 공통정보 가져오기 - 조건 : 폐기여부 F and 폐기여부 T + 요청 미확인 and 폐기여부 T + 요청 거절  하나 조회
    @Override
    public Optional<CommonAsset> findLatestApprovedAsset(String assetCode) {
        CommonAsset result = queryFactory
                .selectFrom(QCommonAsset.commonAsset)  // QCommonAsset 사용
                .where(QCommonAsset.commonAsset.assetCode.eq(assetCode)
                        .and(QCommonAsset.commonAsset.disposalStatus.eq(false)
                                .or(QCommonAsset.commonAsset.disposalStatus.eq(true)
                                        .and(QCommonAsset.commonAsset.approval.eq(Approval.valueOf("UNCONFIRMED")))
                                .or(QCommonAsset.commonAsset.disposalStatus.eq(true)
                                        .and(QCommonAsset.commonAsset.approval.eq(Approval.valueOf("REFUSAL")))
                                )
                                )
                        )
                )
                .orderBy(QCommonAsset.commonAsset.assetNo.desc()) // 최신 데이터부터 정렬
                .fetchFirst(); // 가장 최신의 하나만 가져옴

        return Optional.ofNullable(result);
    }

    // 자산목록 (자산 공통정보) - 조건 : 폐기여부 F and 폐기여부 T + 요청 미확인 and 폐기여부 T + 요청 거절   리스트 조회
    @Override
    public List<CommonAsset> findApprovedAndNotDisposedAssets() {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 서브쿼리: 각 자산 코드별 최대 자산 번호 찾기
        JPAQuery<Long> subQuery = new JPAQuery<Long>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset;

        subQuery.select(subCa.assetNo.max())
                .from(subCa)
                .where(subCa.disposalStatus.isFalse()
                                .or(
                                        subCa.disposalStatus.isTrue()
                                                .and(subCa.approval.eq(Approval.valueOf("UNCONFIRMED")))
                                        .or(
                                                subCa.disposalStatus.isTrue()
                                                        .and(subCa.approval.eq(Approval.valueOf("REFUSAL")))
                                        )
                                )
                )
                .groupBy(subCa.assetCode);

        // 메인 쿼리: 최신 자산 정보 조회
        JPAQuery<CommonAsset> query = new JPAQuery<CommonAsset>(entityManager);

        return query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery))
                .fetch();
    }

    //위치에 따른 자산 목록
    @Override
    public List<CommonAsset> findDetailByLocation(AssetLocation location) {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 서브쿼리: 각 자산 코드별 최대 자산 번호 찾기
        JPAQuery<Long> subQuery = new JPAQuery<Long>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset;

        subQuery.select(subCa.assetNo.max())
                .from(subCa)
                .where(subCa.disposalStatus.isFalse()
                        .and(subCa.assetLocation.eq(location))
                        .or(
                                subCa.disposalStatus.isTrue()
                                        .and(subCa.approval.eq(Approval.valueOf("UNCONFIRMED")))
                                        .or(
                                                subCa.disposalStatus.isTrue()
                                                        .and(subCa.approval.eq(Approval.valueOf("REFUSAL")))
                                        )
                        )
                )
                .groupBy(subCa.assetCode);

        // 메인 쿼리: 최신 자산 정보 조회
        JPAQuery<CommonAsset> query = new JPAQuery<CommonAsset>(entityManager);

        return query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery))
                .fetch();
    }

    // 자산 분류에 따른 최신 자산 코드 가져오기
    @Override
    public Optional<String> findLastAssetCodeByClassification(AssetClassification classification) {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 최신 자산 코드 가져오기
        String assetCode = queryFactory
                .select(ca.assetCode)
                .from(ca)
                .where(ca.assetClassification.eq(classification))
                .orderBy(ca.assetNo.desc())  // 최신 자산을 먼저 정렬
                .fetchFirst();

        return Optional.ofNullable(assetCode);
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
    // 수정 이력
    @Override
    public List<CommonAsset> findApprovedAssetsByCodeExceptLatest(String assetCode) {
        QCommonAsset commonAsset = QCommonAsset.commonAsset;

        return queryFactory
                .selectFrom(commonAsset)
                .where(commonAsset.assetCode.eq(assetCode)
                        .and(commonAsset.approval.eq(Approval.APPROVE))
                        .and(commonAsset.assetNo.ne(
                                JPAExpressions
                                        .select(commonAsset.assetNo.max())
                                        .from(commonAsset)
                                        .where(commonAsset.assetCode.eq(assetCode)
                                                .and(commonAsset.approval.eq(Approval.APPROVE)))
                        )))
                .fetch();
    }

    // 자산 수정할때 하나의 assetCode 중에 데이터가 여러개일때 최신 assetNo 조회 하는 쿼리
    @Override
    public Optional<CommonAsset> findLatestAssetCode(String assetCode) {

        CommonAsset result = queryFactory
                .selectFrom(QCommonAsset.commonAsset)
                .where(QCommonAsset.commonAsset.assetCode.eq(assetCode))
                .orderBy(QCommonAsset.commonAsset.assetNo.desc()) // assetNo 기준으로 내림차순 정렬
                .fetchFirst(); // 가장 높은 assetNo를 가진 하나의 결과를 가져옴

        return Optional.ofNullable(result);

    }
}
