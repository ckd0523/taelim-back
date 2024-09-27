package com.codehows.taelim.dto;

import com.codehows.taelim.constant.CarType;
import com.codehows.taelim.constant.EngineType;
import com.codehows.taelim.entity.Car;
import com.codehows.taelim.entity.CommonAsset;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    private Long carNo;

    private CommonAsset assetNo;

    private Long displacement;
    private Long doorsCount;

    private EngineType engineType;

    private CarType carType;

    private String identificationNo;
    private String carColor;
    private Long modelYear;

    // DTO에서 엔티티로 변환
    public Car toEntity() {
        return Car.builder()
                .carNo(this.carNo)
                .assetNo(this.assetNo) // CommonAsset 객체 그대로 전달
                .displacement(this.displacement)
                .doorsCount(this.doorsCount)
                .engineType(this.engineType)
                .carType(this.carType)
                .identificationNo(this.identificationNo)
                .carColor(this.carColor)
                .modelYear(this.modelYear)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static CarDto fromEntity(Car entity) {
        return new CarDto(
                entity.getCarNo(),
                entity.getAssetNo(),  // CommonAsset 그대로 매핑
                entity.getDisplacement(),
                entity.getDoorsCount(),
                entity.getEngineType(),
                entity.getCarType(),
                entity.getIdentificationNo(),
                entity.getCarColor(),
                entity.getModelYear()
        );
    }
}
