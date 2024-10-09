package com.codehows.taelim.repository;

import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairFileRepository extends JpaRepository<RepairFile, Long> {
    List<RepairFile> findByRepairNo(RepairHistory repairHistory);
    RepairFile findByRepairNoAndRepairType(RepairHistory repairNo, RepairType repairType);
    
}
