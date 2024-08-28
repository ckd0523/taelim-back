package com.codehows.taelim.repository;

import com.codehows.taelim.entity.Car;
import com.codehows.taelim.entity.CommonAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByAssetNo(CommonAsset commonAsset);
}
