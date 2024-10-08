package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.ItSystemEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItSystemEquipmentRepository extends JpaRepository<ItSystemEquipment, Long> {
    ItSystemEquipment findByAssetNo(CommonAsset commonAsset);
}
