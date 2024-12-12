package com.codehows.taelim.repository;

import com.codehows.taelim.entity.QRPrinter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QrPrinterRepository extends JpaRepository<QRPrinter, Long> {
    List<QRPrinter> findAll(); // 모든 프린터 조회

    Optional<QRPrinter> findByIsSelectedTrue();
}
