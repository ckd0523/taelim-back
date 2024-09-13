package com.codehows.taelim.repository;

import com.codehows.taelim.entity.DemandDtl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandDtlRepository extends JpaRepository<DemandDtl, Long> , DemandDtlRepositoryCustom {

}
