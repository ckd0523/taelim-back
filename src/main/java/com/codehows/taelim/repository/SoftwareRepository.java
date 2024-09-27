package com.codehows.taelim.repository;

import com.codehows.taelim.dto.SoftwareDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Software;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
    Software findByAssetNo(CommonAsset assetNo);

}
