package com.codehows.taelim.repository;

import com.codehows.taelim.entity.AmountSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AmountSetRepository extends JpaRepository<AmountSet, Long> {
    AmountSet findAmountSetByValueStandardNo(Long no);

    @Modifying
    @Transactional
    @Query("UPDATE AmountSet a SET a.highValueStandard = :highValueStandard, a.lowValueStandard = :lowValueStandard WHERE a.valueStandardNo = 1")
    void updateAmountSet(@Param("highValueStandard") Long highValueStandard,
                         @Param("lowValueStandard") Long lowValueStandard);

    @Modifying
    @Query(value = "INSERT INTO amount_set (high_value_standard, low_value_standard) VALUES (?1, ?2)", nativeQuery = true)
    void insertAmountSet(Long amount1, Long amount2);

    // valueStandardNo로 AmountSet을 조회
    AmountSet findByValueStandardNo(Long valueStandardNo);

}
