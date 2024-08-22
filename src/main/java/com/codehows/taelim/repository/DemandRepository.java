package com.codehows.taelim.repository;

import com.codehows.taelim.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, Long> {
}
