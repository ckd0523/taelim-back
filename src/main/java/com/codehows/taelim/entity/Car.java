package com.codehows.taelim.entity;

import com.codehows.taelim.constant.CarType;
import com.codehows.taelim.constant.EngineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "car")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Car {

    @Id
    @Column(name = "carNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private Long displacement;
    private Long doorsCount;

    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private String identificationNo;
    private String carColor;
    private Long modelYear;
}
