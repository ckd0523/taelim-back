package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.InformationProtectionSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationProtectionSystemRepository extends JpaRepository<InformationProtectionSystem, Long> {
    InformationProtectionSystem findByAssetNo(CommonAsset commonAsset);
}
