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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                                .and(QCommonAsset.commonAsset.approval.eq(Approval.valueOf("APPROVE")))
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

        // 1. 폐기된 자산 (approval = APPROVE && disposal = TRUE)을 가진 assetCode를 필터링
        JPAQuery<String> excludedAssetCodes = new JPAQuery<>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset; // 서브 쿼리용 Q객체

        excludedAssetCodes.select(subCa.assetCode)
                .from(subCa)
                .where(
                        subCa.approval.eq(Approval.APPROVE)
                                .and(subCa.disposalStatus.isTrue())
                )
                .groupBy(subCa.assetCode);

        // 2. 최신 assetNo를 선택하는 서브 쿼리
        JPAQuery<Long> subQuery = new JPAQuery<>(entityManager);
        subQuery.select(ca.assetNo.max())
                .from(ca)
                .where(
                        ca.assetCode.notIn(excludedAssetCodes) // 폐기된 assetCode 제외
                                .and(ca.approval.eq(Approval.APPROVE)) // APPROVE 상태인 자산만
                                .and(ca.disposalStatus.isFalse()) // disposal이 False인 자산만
                )
                .groupBy(ca.assetCode); // assetCode 기준으로 그룹화

        // 3. 최종 쿼리: 최신 assetNo에 해당하는 자산 조회
        JPAQuery<CommonAsset> query = new JPAQuery<>(entityManager);
        return query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery)) // 서브 쿼리에서 선택된 최신 assetNo
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

    // 자산 수정이력에서 상세정보화면 최신 자산과 그 이전 자산 가져오는 쿼리
   @Override
    public List<CommonAsset> findNextAssetsByAssetNo(Long assetNo) {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 주어진 assetNo를 기준으로 자산 코드 가져오기
        CommonAsset modifiedAsset  = queryFactory
                .selectFrom(ca)
                .where(ca.assetNo.eq(assetNo)) // 수정된 자산 조회
                .fetchOne();

        if (modifiedAsset  == null) {
            throw new RuntimeException("해당 assetNo에 대한 자산을 찾을 수 없습니다.");
        }

        String assetCode = modifiedAsset.getAssetCode();

       // 동일한 assetCode의 자산을 조회 (최신 자산 2개를 내림차순으로 가져옴)
       List<CommonAsset> assets = queryFactory
               .selectFrom(ca)
               .where(ca.assetCode.eq(assetCode) // 동일한 assetCode 필터링
                       .and(ca.approval.eq(Approval.APPROVE)) // 주어진 assetCode 에서 approve 인 자산 필터
                       .and(ca.assetNo.loe(assetNo))) // 주어진 assetNo보다 작은 자산 필터링
               .orderBy(ca.assetNo.desc()) // 내림차순 정렬 (최신 자산이 먼저 오도록)
               .limit(2) // 최신 자산과 그 직전 자산만 가져오기
               .fetch();

       // 리스트를 오름차순으로 정렬 (assetNo 기준으로)
       return assets.stream()
               .sorted(Comparator.comparing(CommonAsset::getAssetNo)) // assetNo 기준 오름차순 정렬
               .collect(Collectors.toList());
    }

    // 자산 수정이력에서 상세정보화면 최신 자산과 그 이전 자산 가져오는 쿼리
    @Override
    public CommonAsset findNextAssetByAssetNo(Long assetNo) {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 주어진 assetNo를 기준으로 자산 코드 가져오기
        CommonAsset modifiedAsset = queryFactory
                .selectFrom(ca)
                .where(ca.assetNo.eq(assetNo)) // 수정된 자산 조회
                .fetchOne();

        if (modifiedAsset == null) {
            throw new RuntimeException("해당 assetNo에 대한 자산을 찾을 수 없습니다.");
        }

        String assetCode = modifiedAsset.getAssetCode();

        // 동일한 assetCode의 자산 중 최신 자산 하나를 내림차순으로 가져옴
        CommonAsset asset = queryFactory
                .selectFrom(ca)
                .where(ca.assetCode.eq(assetCode) // 동일한 assetCode 필터링
                        .and(ca.approval.eq(Approval.APPROVE)) // 승인된 자산 필터링
                        .and(ca.assetNo.loe(assetNo))) // 주어진 assetNo보다 작거나 같은 자산
                .orderBy(ca.assetNo.desc()) // 최신 자산이 먼저 오도록 내림차순 정렬
                .fetchFirst(); // 하나만 가져오기

        return asset;
    }

    @Override
    public Approval findAssetApprovalByAssetCode(String assetCode) {

        QCommonAsset commonAsset = QCommonAsset.commonAsset;

        CommonAsset latestAsset = queryFactory
                .selectFrom(commonAsset)
                .where(commonAsset.assetCode.eq(assetCode))
                .orderBy(commonAsset.assetNo.desc())
                .fetchFirst(); ;  // 가장 첫 번째 데이터만 조회

        // 만약 최신 자산이 없을 경우 null 반환
        return latestAsset != null ? latestAsset.getApproval() : null;

    }

    @Override
    public List<CommonAsset> findApprovedAssetsByAssetCode(String assetCode) {
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        return queryFactory
                .selectFrom(commonAsset)
                .where(commonAsset.assetCode.eq(assetCode)
                        .and(commonAsset.approval.eq(Approval.APPROVE)))
                .fetch();
    }
}
