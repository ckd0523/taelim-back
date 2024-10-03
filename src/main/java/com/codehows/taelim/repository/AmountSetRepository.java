package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AmountSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AmountSetRepository extends JpaRepository<AmountSet, Long> {
    AmountSet findAmountSetByValueStandardNo(Long no);

    @Modifying
    @Transactional
    @Query("UPDATE AmountSet a SET a.highValueStandard = :highValueStandard, a.lowValueStandard = :lowValueStandard WHERE a.valueStandardNo = 1")
    void updateAmountSet(@Param("highValueStandard") Long highValueStandard,
                         @Param("lowValueStandard") Long lowValueStandard);
}
