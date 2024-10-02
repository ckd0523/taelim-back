package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.RepairHistory;

import java.util.List;

public interface RepairHistoryRepositoryCustom {
    List<RepairHistory> findByAssetCode(String assetCode);
}
