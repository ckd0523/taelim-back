package com.codehows.taelim.repository;

import com.codehows.taelim.entity.DemandDtl;

import java.util.List;

public interface DemandDtlRepositoryCustom {

    // 폐기이력 불러오기
    List<DemandDtl> findDeleteHistory();

    List<DemandDtl> findUpdateHistory();
}
