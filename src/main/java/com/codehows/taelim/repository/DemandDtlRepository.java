package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.DemandDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandDtlRepository extends JpaRepository<DemandDtl, Long> , DemandDtlRepositoryCustom {
    List<DemandDtl> findByDemandNo_DemandNo(Long demandNo);

    public List<DemandDtl> findAllByOrderByDemandDtlNoDesc();

    DemandDtl findByAssetNo(CommonAsset assetNo);
}
