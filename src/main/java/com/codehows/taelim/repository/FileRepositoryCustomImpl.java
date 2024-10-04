package com.codehows.taelim.repository;

import com.codehows.taelim.entity.File;
import com.codehows.taelim.entity.QCommonAsset;
import com.codehows.taelim.entity.QFile;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

        JPAQuery<File> subQuery = new JPAQuery<>(entityManager);

        return subQuery.select(file)
                .from(file)
                .join(commonAsset).on(file.assetNo.assetNo.eq(commonAsset.assetNo))
                .where(commonAsset.assetCode.eq(assetCode))
                .fetch();
    }
}
