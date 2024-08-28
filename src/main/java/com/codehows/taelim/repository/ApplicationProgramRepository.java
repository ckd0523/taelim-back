package com.codehows.taelim.repository;

import com.codehows.taelim.entity.ApplicationProgram;
import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationProgramRepository extends JpaRepository<ApplicationProgram, Long> {
    ApplicationProgram findByAssetNo(CommonAsset commonAsset);
}
