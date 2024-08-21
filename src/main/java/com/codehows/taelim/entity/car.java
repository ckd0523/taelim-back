package com.codehows.taelim.entity;

import com.codehows.taelim.constant.carType;
import com.codehows.taelim.constant.engineType;
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
public class car {

    @Id
    @Column(name = "carNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private Long displacement;
    private Long doorsCount;
    private engineType engineType;
    private carType carType;
    private String identificationNo;
    private String carColor;
    private Long modelYear;
}
