package com.codehows.taelim.repository;

import com.codehows.taelim.constant.*;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommonAssetRepository extends JpaRepository<CommonAsset, Long>, CommonAssetRepositoryCustom {

    CommonAsset findTopByOrderByAssetNoDesc();

    Optional<CommonAsset> findByAssetCode(String assetCode);

    // 분류에 맞느 자산을 필터링해서 가져오는 메서드
    //List<CommonAsset> findByClassification(AssetClassification assetClassification);


//    // 특정 필드를 기준으로 검색하는 메서드
//    @Query("SELECT a FROM CommonAsset a WHERE "
//            + "(:assetName IS NULL OR a.assetName LIKE %:assetName%) AND "
//            + "((:assetLocationString IS NULL AND :assetLocationEnum IS NULL) OR "
//            + "(a.assetLocation = :assetLocationString OR a.assetLocation = :assetLocationEnum)) AND "
//            + "(:assetUser IS NULL OR a.assetUser.uName LIKE %:assetUser%) AND " // assetUser 필드에서 username으로 검색
//            + "((:departmentString IS NULL AND :departmentEnum IS NULL) OR "
//            + "(a.department = :departmentString OR a.department = :departmentEnum)) AND "
//            + "(:introducedDate IS NULL OR a.introducedDate = :introducedDate)")
//    Page<CommonAsset> searchAssets(
//            @Param("assetName") String assetName,
//            @Param("assetLocationString") String assetLocationString,
//            @Param("assetLocationEnum") AssetLocation assetLocationEnum,
//            @Param("assetUser") String assetUser, // 여전히 String으로 받아서 username으로 비교
//            @Param("departmentString") String departmentString,
//            @Param("departmentEnum") Department departmentEnum,
//            @Param("introducedDate") LocalDate introducedDate,
//            Pageable pageable);

    Long countByDepartmentAndAssetClassification(Department department, AssetClassification assetClassification);

    Long countByOperationStatus(OperationStatus operationStatus);

}
