package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "demandDtl")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DemandDtl {

    @Id
    @Column(name = "demandDtlNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long demandDtlNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandNo")
    private Demand demandNo;

}
