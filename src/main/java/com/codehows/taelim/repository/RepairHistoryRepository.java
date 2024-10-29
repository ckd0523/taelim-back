package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import com.codehows.taelim.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long>, RepairHistoryRepositoryCustom {
    List<RepairHistory> findByAssetNo(CommonAsset assetNo);

    //유지보수 중인 레코드 가져오기, 없으면 return 0
    @Query("Select count(c) from RepairHistory c where c.repairStatus = '진행중'")
    Long findRepairingAmount();
}
