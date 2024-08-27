package com.codehows.taelim.repository;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.ElectronicInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectronicInformationRepository extends JpaRepository<ElectronicInformation, Long> {
    ElectronicInformation findByAssetNo(CommonAsset commonAsset);
}
