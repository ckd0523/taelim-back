package com.codehows.taelim.dto;

import com.codehows.taelim.constant.CarType;
import com.codehows.taelim.constant.EngineType;
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
}
