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
public class RepairHistoryRepositoryCustomImpl implements RepairHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RepairHistory> findByAssetCode(String assetCode){
        QRepairHistory repairHistory = QRepairHistory.repairHistory;
        QCommonAsset commonAsset = QCommonAsset.commonAsset;
        QRepairFile repairFile = QRepairFile.repairFile;

        // 서브쿼리
        JPAQuery<RepairHistory> subQurey = new JPAQuery<>(entityManager);

        // 쿼리 작성
        return subQurey.select(repairHistory)
                .from(repairHistory)
                .join(commonAsset).on(repairHistory.assetNo.assetNo.eq(commonAsset.assetNo)) // RepairHistory와 CommonAsset 조인
                .leftJoin(repairFile).on(repairFile.repairNo.repairNo.eq(repairHistory.repairNo)) // RepairFile의 repairNo를 사용하여 조인
                .where(commonAsset.assetCode.eq(assetCode)) // assetCode로 필터링
                .fetch(); // 결과 반환
    }
}
