package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.PatentsAndTrademarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatentsAndTrademarksRepository extends JpaRepository<PatentsAndTrademarks, Long> {
    PatentsAndTrademarks findByAssetNo(CommonAsset commonAsset);
}
