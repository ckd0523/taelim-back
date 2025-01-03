package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import com.codehows.taelim.entity.QCommonAsset;
import com.codehows.taelim.entity.QFile;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FileRepositoryCustomImpl implements FileRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    public List<File> findByAssetCode(String assetCode) {
        QFile file = QFile.file;
        QCommonAsset commonAsset = QCommonAsset.commonAsset;

        // 가장 최신 assetNo를 가진 CommonAsset을 먼저 조회
        Long latestAssetNo = new JPAQuery<>(entityManager)
                .select(commonAsset.assetNo) // assetNo만 선택
                .from(commonAsset)
                .where(commonAsset.assetCode.eq(assetCode))
                .orderBy(commonAsset.assetNo.desc()) // 최신 assetNo 기준으로 정렬
                .fetchFirst(); // 최신 assetNo 하나 가져오기

        // latestAssetNo가 null인 경우 빈 리스트를 반환
        if (latestAssetNo == null) {
            return Collections.emptyList();
        }

        // 최신 assetNo에 해당하는 파일을 조회
        return new JPAQuery<>(entityManager)
                .select(file)
                .from(file)
                .where(file.assetNo.assetNo.eq(latestAssetNo)) // assetNo로 조건 설정
                .fetch(); // 모든 파일 결과 반환
    }

    @Override
    public File findByFileName1(String fileName) {
        QFile file = QFile.file;

        // fileName으로 파일을 조회하고, 최신 fileNo를 기준으로 정렬하여 첫 번째 파일을 반환
        return new JPAQuery<>(entityManager)
                .select(file)
                .from(file)
                .where(file.fileName.eq(fileName)) // fileName으로 조건 설정
                .orderBy(file.fileNo.desc()) // 최신 fileNo 기준으로 정렬
                .fetchFirst(); // 가장 첫 번째 파일 반환
    }


}
