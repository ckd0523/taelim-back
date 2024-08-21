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
public class furniture {
    @Id
    @Column(name = "furnitureNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long furnitureNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String funitureSize;
}
