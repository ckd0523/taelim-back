package com.codehows.taelim.repository;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.UserDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.QCommonAsset;
import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
import com.codehows.taelim.service.UserService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class CommonAssetRepositoryCustomImpl implements CommonAssetRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @PersistenceContext
    private EntityManager entityManager;
    private final AspNetUserRepository aspNetUserRepository;
    private final UserService userService;

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

    //소유 자산 총액 가져오기
    @Override
    public Long findTotalOwnedPurchaseCost() {
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

        // 3. 최종 쿼리: 최신 assetNo에 해당하는 자산의 purchaseCost 합계 조회
        JPAQuery<Long> query = new JPAQuery<>(entityManager);
        return query.select(ca.purchaseCost.sum()) // purchaseCost의 합계
                .from(ca)
                .where(ca.assetNo.in(subQuery)
                        .and(ca.ownership.eq(Ownership.OWNED))) // 서브 쿼리에서 선택된 최신 assetNo
                .fetchOne();
    }

    //국책과제 + 기타 자산 총액 가져오기
    @Override
    public Long findTotalLeasedPurchaseCost() {
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

        // 3. 최종 쿼리: 최신 assetNo에 해당하는 자산의 purchaseCost 합계 조회
        JPAQuery<Long> query = new JPAQuery<>(entityManager);
        return query.select(ca.purchaseCost.sum()) // purchaseCost의 합계
                .from(ca)
                .where(ca.assetNo.in(subQuery)
                        .and(ca.ownership.eq(Ownership.NATIONAL_PROJECT)
                                )
                                .or(ca.ownership.eq(Ownership.ETC))

                        ) // 서브 쿼리에서 선택된 최신 assetNo
                .fetchOne();
    }

    //총 자산 개수 가져오기
    @Override
    public Long findTotalAssetAmount() {
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

        // 3. 최종 쿼리: 최신 assetNo에 해당하는 자산의 purchaseCost 합계 조회
        JPAQuery<Long> query = new JPAQuery<>(entityManager);
        return query.select(ca.count())
                .from(ca)
                .where(ca.assetNo.in(subQuery)) // 서브 쿼리에서 선택된 최신 assetNo
                .fetchOne();
    }

    //자산 분류별 자산 개수
    public Map<AssetClassification, Long> assetsClassificationCounts() {
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

        // 3. 각 자산 분류별 자산 개수 조회
        JPAQuery<Tuple> query = new JPAQuery<>(entityManager);
        QCommonAsset ca2 = QCommonAsset.commonAsset;

        List<Tuple> results = query.select(
                        ca2.assetClassification,
                        ca2.countDistinct()
                )
                .from(ca2)
                .where(
                        ca2.assetCode.notIn(excludedAssetCodes)
                                .and(ca2.assetNo.in(subQuery))
                                .and(ca2.approval.eq(Approval.APPROVE))
                                .and(ca2.disposalStatus.isFalse())
                )
                .groupBy(ca2.assetClassification)
                .fetch();

        Map<AssetClassification, Long> assetClassificationCounts = new HashMap<>();
        for (Tuple result : results) {
            // Get the asset classification as a string (in case it's an enum, convert to string)
            AssetClassification assetClassification = result.get(ca2.assetClassification);
            // Get the count as Long
            Long count = result.get(1, Long.class); // Retrieve count from the tuple
            assetClassificationCounts.put(assetClassification, count);
        }
        return assetClassificationCounts;
    }

    //부서별 자산 개수
    public Map<Department, Long> departmentLongMap() {
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

        JPAQuery<Tuple> query = new JPAQuery<>(entityManager);

        // 3. 각 자산 분류별 자산 개수 조회
        QCommonAsset ca2 = QCommonAsset.commonAsset;

        List<Tuple> results = query.select(
                        ca2.department,
                        ca2.countDistinct()
                )
                .from(ca2)
                .where(
                        ca2.assetCode.notIn(excludedAssetCodes)
                                .and(ca2.assetNo.in(subQuery))
                                .and(ca2.approval.eq(Approval.APPROVE))
                                .and(ca2.disposalStatus.isFalse())
                )
                .groupBy(ca2.department)
                .fetch();

        Map<Department, Long> departmentLongHashMap = new HashMap<>();
        for (Tuple result : results) {
            Department department = result.get(ca2.department);
            Long count = result.get(1, Long.class);
            departmentLongHashMap.put(department, count);
        }
        return departmentLongHashMap;
    }

    //부서별 자산 분류별 개수
    public Map<Department, Map<AssetClassification, Long>> getDepartmentAssetClassificationAmount(){
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

        JPAQuery<Tuple> query = new JPAQuery<>(entityManager);

        // 3. 각 자산 분류별 자산 개수 조회
        QCommonAsset ca2 = QCommonAsset.commonAsset;

        List<Tuple> results = query.select(
                        ca2.department,
                        ca2.assetClassification,
                        ca2.countDistinct()
                )
                .from(ca2)
                .where(
                        ca2.assetCode.notIn(excludedAssetCodes)
                                .and(ca2.assetNo.in(subQuery))
                                .and(ca2.approval.eq(Approval.APPROVE))
                                .and(ca2.disposalStatus.isFalse())
                )
                .groupBy(ca2.department, ca2.assetClassification)
                .fetch();

        Map<Department, Map<AssetClassification, Long>> departmentMapMap = new EnumMap<>(Department.class);

        for(Tuple tuple : results) {
            Department department = tuple.get(ca2.department);
            AssetClassification assetClassification = tuple.get(ca2.assetClassification);
            Long count = tuple.get(ca2.countDistinct());

            departmentMapMap.computeIfAbsent(department, k -> new EnumMap<>(AssetClassification.class))
                    .put(assetClassification, count);
        }

        return departmentMapMap;
    }

    //자산총액추이
    @Override
    public Map<Integer, Long> findAssetPurchaseSum(int year) {
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

        int startYear = LocalDate.of(2000, 1,1).getYear();
        int currentYear = LocalDate.now().getYear();
        int endYear = Math.min(year + 5, currentYear);

        // 3. 최종 쿼리: 최신 assetNo에 해당하는 자산의 purchaseCost 합계 조회
       JPAQuery<Tuple> query = new JPAQuery<>(entityManager);
       List<Tuple> results = query.select(ca.purchaseDate.year(), ca.purchaseCost.sum())
               .from(ca)
               .where(ca.purchaseDate.year().between(startYear, endYear)
                       .and(ca.assetNo.in(subQuery)))
               .groupBy(ca.purchaseDate.year())
               .fetch();

       Map<Integer, Long> yearToCumulativeSum = new TreeMap<>();
       long cumulativeSum = 0;
       Map<Integer, Long> yearlySums = results.stream()
               .filter(tuple -> tuple.get(ca.purchaseDate.year()) != null)
               .collect(Collectors.toMap(
                       tuple -> tuple.get(ca.purchaseDate.year()),
                       tuple -> tuple.get(ca.purchaseCost.sum()),
                       Long :: sum,
                       TreeMap::new
               ));
       for(int currentYearIter = startYear; currentYearIter <= endYear; currentYearIter++) {
           cumulativeSum += yearlySums.getOrDefault(currentYearIter, 0L);
           yearToCumulativeSum.put(currentYearIter, cumulativeSum);
       }
       return yearToCumulativeSum;
    }

    //등급별 자산 개수
    public Map<String, Long> getAssetGrades() {
        QCommonAsset ca = QCommonAsset.commonAsset;

        // 1. 폐기된 자산의 assetCode 필터링
        JPAQuery<String> excludedAssetCodes = new JPAQuery<>(entityManager);
        excludedAssetCodes.select(ca.assetCode)
                .from(ca)
                .where(ca.approval.eq(Approval.APPROVE)
                        .and(ca.disposalStatus.isTrue()))
                .groupBy(ca.assetCode);

        // 2. 최신 assetNo를 선택하는 서브 쿼리
        JPAQuery<Long> subQuery = new JPAQuery<>(entityManager);
        subQuery.select(ca.assetNo.max())
                .from(ca)
                .where(ca.assetCode.notIn(excludedAssetCodes.fetch())
                        .and(ca.approval.eq(Approval.APPROVE))
                        .and(ca.disposalStatus.isFalse()))
                .groupBy(ca.assetCode);

        // 3. 필터링된 자산 가져오기
        JPAQuery<CommonAsset> query = new JPAQuery<>(entityManager);
        List<CommonAsset> filteredAssets = query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery.fetch())) // 최신 assetNo만 가져오기
                .fetch();

        // 4. 등급별로 그룹화
        return filteredAssets.stream()
                .collect(Collectors.groupingBy(
                        asset -> {
                            int total = asset.getConfidentiality() + asset.getIntegrity() + asset.getAvailability();
                            if (total >= 7) return "A";
                            else if (total >= 5) return "B";
                            else return "C";
                        },
                        Collectors.counting()
                ));
    }


    //소유권별
    public Map<Ownership, Long> findOwnershipCounts() {
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

        JPAQuery<Tuple> query = new JPAQuery<>(entityManager);
        List<Tuple> results = query.select(
                ca.ownership,
                ca.count()
        )
                .from(ca)
                .where(
                        ca.assetCode.notIn(excludedAssetCodes)
                                .and(ca.assetNo.in(subQuery))
                                .and(ca.approval.eq(Approval.APPROVE))
                                .and(ca.disposalStatus.isFalse())
                )
                .groupBy(ca.ownership)
                .fetch();

        Map<Ownership, Long> ownershipLongMap = new EnumMap<>(Ownership.class);
        for(Tuple result : results) {
            Ownership ownership = result.get(ca.ownership);
            Long count = result.get(ca.count());
            ownershipLongMap.put(ownership, count);
        }

        return ownershipLongMap;

    }

    // 대시보드 운용현황
    public Map<OperationStatus, Long> findOprtationStatusCounts() {
        QCommonAsset ca =QCommonAsset.commonAsset;
        // 1.폐기된 자산 (approval = APPROVE && dispoal = TRUE)을 가진 assetCode 를 필터링
        JPAQuery<String> excludedAssetCodes =new JPAQuery<>(entityManager);
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
                        ca.assetCode.notIn(excludedAssetCodes) // 폐기된 assetCode제외
                                .and(ca.approval.eq(Approval.APPROVE)) // APPROVE 상태인 자사만
                                .and(ca.disposalStatus.isFalse()) // disposal이 False 이 자산만
                )
                .groupBy(ca.assetCode); // assetCode 기준으로 그룹화
        // 3. 최신 자산 필터링
        JPAQuery<CommonAsset> query = new JPAQuery<>(entityManager);
        List<CommonAsset> filteredAssets = query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery.fetch())) // 최신 assetNo만 포함
                .fetch();

        // 4. OperationStatus 별로 그룹화
        return filteredAssets.stream()
                .collect(Collectors.groupingBy(
                        asset -> {
                            // 문자열 상태를 OperationStatus로 변환
                            return OperationStatus.from(asset.getOperationStatus().getDescription());
                        },
                        Collectors.counting()
                ));
    }


    //폐기가 다가오는 자산의 물품
    public Map<AssetClassification, Long> findAssetsNearEndOfLife() {
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

        // Calculate the end of the 3-month period
        LocalDate referenceDate = LocalDate.now();
        LocalDate endOfPeriod = referenceDate.plusMonths(3);

        JPAQuery<Tuple> query = new JPAQuery<>(entityManager);
        List<Tuple> results = query.select(ca.assetClassification, ca.assetNo.count())
                .from(ca)
                .where(
                        ca.purchaseDate.year().add(ca.usefulLife.intValue()).between(referenceDate.getYear(), endOfPeriod.getYear())
                                .and(ca.purchaseDate.month().add(ca.usefulLife.intValue()).between(referenceDate.getMonthValue(), endOfPeriod.getMonthValue()))
                                .and(ca.assetNo.in(subQuery))
                )
                .groupBy(ca.assetClassification)
                .fetch();

        return results.stream().collect(Collectors.toMap(
                tuple -> tuple.get(ca.assetClassification),
                tuple -> tuple.get(ca.assetNo.count())
        ));
    }
    //위치별 자산 개수
    public Map<AssetClassification, Long> findAssetsByAssetLocation(AssetLocation assetLocation) {
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
                                .and(ca.assetLocation.eq(assetLocation)) // 필터링된 location만
                )
                .groupBy(ca.assetCode); // assetCode 기준으로 그룹화

        JPAQuery<Tuple> assetCountQuery = new JPAQuery<>(entityManager);
        List<Tuple> results = assetCountQuery.select(ca.assetClassification, ca.assetNo.count())
                .from(ca)
                .where(ca.assetNo.in(subQuery))
                .groupBy(ca.assetClassification)
                .fetch();

        Map<AssetClassification, Long> assetCountByLocation = new HashMap<>();
        for(Tuple result : results) {
            AssetClassification assetClassification = result.get(ca.assetClassification);
            Long count = result.get(ca.assetNo.count());

            assetCountByLocation.put(assetClassification, count);
        }
        return assetCountByLocation;

    }

//    public Map
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

    @Override
    public List<CommonAsset> findNextAssetsByAssetNo1(Long assetNo) {
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

        // 동일한 assetCode의 자산을 조회 (최신 자산 1개만 가져옴)
        CommonAsset asset = queryFactory
                .selectFrom(ca)
                .where(ca.assetCode.eq(assetCode) // 동일한 assetCode 필터링
                        .and(ca.approval.eq(Approval.APPROVE)) // 주어진 assetCode에서 approve된 자산 필터링
                        .and(ca.assetNo.loe(assetNo))) // 주어진 assetNo보다 작은 자산 필터링
                .orderBy(ca.assetNo.desc()) // 내림차순 정렬 (최신 자산이 먼저 오도록)
                .limit(1) // 최신 자산 하나만 가져오기
                .fetchOne();

        // 리스트에 modifiedAsset과 asset을 추가
        List<CommonAsset> assets = new ArrayList<>();
        assets.add(modifiedAsset);  // modifiedAsset 추가
        if (asset != null) {        // asset이 null이 아닐 경우에만 추가
            assets.add(asset);
        }

        // 리스트를 assetNo 기준으로 오름차순 정렬
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

    @Override
    public Page<CommonAsset> findApprovedAndNotDisposedAssetsWithSearch(
            String assetName,
            AssetLocation assetLocationEnum,
            String assetUser,  // 외부 DB의 user ID
            Department departmentEnum,
            LocalDate startDate, // 검색 범위 시작 날짜
            LocalDate endDate,   // 검색 범위 종료 날짜
            AssetClassification assetClassification,
            Pageable pageable) {

        QCommonAsset ca = QCommonAsset.commonAsset;

        // 1. 폐기된 자산 (approval = APPROVE && disposal = TRUE)을 가진 assetCode를 필터링
        JPAQuery<String> excludedAssetCodes = new JPAQuery<>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset;

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
                                .and(ca.approval.eq(Approval.APPROVE)) // 승인된 자산만
                                .and(ca.disposalStatus.isFalse()) // 폐기되지 않은 자산만
                )
                .groupBy(ca.assetCode);

        // 3. 최종 쿼리: 최신 assetNo에 해당하는 자산 조회 + 필터링 조건 추가
        JPAQuery<CommonAsset> query = new JPAQuery<>(entityManager);
        BooleanBuilder builder = new BooleanBuilder();

        // 필터 조건을 추가 (각 필드가 null이 아닌 경우에만 조건을 추가)
        if (assetName != null && !assetName.isEmpty()) {
            builder.and(ca.assetName.likeIgnoreCase("%" + assetName + "%"));
        }
        if (assetLocationEnum != null) {
            builder.and(
                    ca.assetLocation.eq(assetLocationEnum)
            );
        }

        // assetUserId를 사용하여 검색 조건 추가
        if (assetUser != null && !assetUser.isEmpty()) {
            // fullname으로 ID 검색
            List<String> userIds = userService.getUserIdsByFullname(assetUser.trim());

            // 사용자 ID 리스트 로그 출력
            System.out.println("User IDs: " + userIds); // 사용자 ID를 출력합니다.

            if (!userIds.isEmpty()) {
                // assetUser가 ID 리스트와 일치하는 자산만 검색
                builder.and(ca.assetUser.in(userIds));
                System.out.println("Builder conditions after adding user IDs: " + builder.getValue());
            } else {
                // userIds가 비어 있을 경우, 결과를 반환하지 않도록 조건 추가
                builder.and(ca.assetUser.isNull()); // 잘못된 입력 시 빈 결과 반환
                System.out.println("No valid users found for input: " + assetUser);
            }
        }

        if (departmentEnum != null) {
            builder.and(
                    ca.department.eq(departmentEnum)
            );
        }

        // 날짜 범위 조건 추가 (startDate 및 endDate가 있을 경우)
        if (startDate != null && endDate != null) {
            builder.and(ca.introducedDate.between(startDate, endDate));
        } else if (startDate != null) {
            builder.and(ca.introducedDate.goe(startDate)); // 시작 날짜만 있는 경우 이후 날짜 포함
        } else if (endDate != null) {
            builder.and(ca.introducedDate.loe(endDate)); // 종료 날짜만 있는 경우 이전 날짜 포함
        }

        // AssetClassification 필터 조건 추가
        if (assetClassification != null) {
            builder.and(ca.assetClassification.eq(assetClassification));
        }

        // 최신 assetNo에 해당하는 자산을 필터링된 결과로 가져오기
        query.select(ca)
                .from(ca)
                .where(ca.assetNo.in(subQuery).and(builder)) // 최신 자산번호 + 필터 조건
                .orderBy(ca.assetNo.desc()) // assetNo를 내림차순으로 정렬
                .offset(pageable.getOffset()) // 페이지네이션 처리
                .limit(pageable.getPageSize());

        List<CommonAsset> assets = query.fetch();

        // 전체 자산 수를 계산하여 페이지네이션 처리
        long total = query.fetchCount();

        return new PageImpl<>(assets, pageable, total);
    }

    // 요청 승인시 이전 요청들 처리하는 로직
    @Override
    public List<CommonAsset> findUnconfirmedAssetsWithSameCodeAndLessThanAssetNo(String assetCode, Long assetNo) {
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        return queryFactory
                .selectFrom(commonAsset)
                .where(
                        commonAsset.assetCode.eq(assetCode) // AssetCode가 같은 조건
                                .and(commonAsset.approval.eq(Approval.UNCONFIRMED)) // Approval이 UNCONFIRMED인 조건
                                .and(commonAsset.assetNo.lt(assetNo)) // 주어진 AssetNo보다 작은 조건
                )
                .fetch(); // 결과를 리스트로 반환
    }
    // 엑셀 출력을 위한 List 전체를 가져오는 조회(조건으로)
    @Override
    public List<CommonAsset> findAssetByExcel(AssetClassification assetClassification) {

        QCommonAsset ca = QCommonAsset.commonAsset;

        // 1. 폐기된 자산(approval = Approve && disposal = TRUE) 을 가진 assetCode로 필터링
        JPAQuery<String> excludedAssetCodes = new JPAQuery<>(entityManager);
        QCommonAsset subCa = QCommonAsset.commonAsset;

        excludedAssetCodes.select(subCa.assetCode)
                .from(subCa)
                .where(
                        subCa.approval.eq(Approval.APPROVE)
                                .and(subCa.disposalStatus.isTrue())
                )
                .groupBy(subCa.assetCode);

        // 2. 최신 assetNo를 하나 선택하는 쿼리
        JPAQuery<Long> subQuery = new JPAQuery<>(entityManager);
        subQuery.select(ca.assetNo.max())
                .from(ca)
                .where(
                        ca.assetCode.notIn(excludedAssetCodes) //폐기된 assetCode 자산 제외
                                .and(ca.approval.eq(Approval.APPROVE)) // 승인된 자산이면서
                                .and(ca.disposalStatus.isFalse()) // 폐기되지 않은 자산 가져오기
                )
                .groupBy(ca.assetCode);

        // 3. 최종 쿼리 : 최신 assetNo에 해당하는 자산조회 + assetClassifiaction 필터링
        JPAQuery<CommonAsset> query = new JPAQuery<>(entityManager);

        // assetClassification 필터 조건
        if (assetClassification != null) {
            query.select(ca)
                    .from(ca)
                    .where(ca.assetNo.in(subQuery) // 최신 자산번호 + assetClassification
                            .and(ca.assetClassification.eq(assetClassification))) // assetClassification 필터링
                    .orderBy(ca.assetNo.asc()); // assetNo를 내림차순으로 정렬
        } else {
            // assetClassification이 null일 경우 전체 자산 조회
            query.select(ca)
                    .from(ca)
                    .where(ca.assetNo.in(subQuery))
                    .orderBy(ca.assetNo.asc());
        }

        // 결과 리스트 반환
        return query.fetch();
    }
}
