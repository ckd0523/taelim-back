package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.ItNetworkEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItNetworkEquipmentRepository extends JpaRepository<ItNetworkEquipment, Long> {
    ItNetworkEquipment findByAssetNo(CommonAsset commonAsset);
}
