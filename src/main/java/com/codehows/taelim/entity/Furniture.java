package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "furniture")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Furniture {
    @Id
    @Column(name = "furnitureNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long furnitureNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String furnitureSize;
}
