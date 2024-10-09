package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> , FileRepositoryCustom {

    List<File> findByAssetNo(CommonAsset assetNo);

    // fileName을 기준으로 파일을 조회하는 메서드
    Optional<File> findByFileName(String fileName);  // UUID 기반의 fileName으로 파일 조회
}
