package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AmountSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountSetRepository extends JpaRepository<AmountSet, Long> {
    AmountSet findAmountSetByValueStandardNo(Long no);
}
