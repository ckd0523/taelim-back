package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "otherAssets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class otherAssets {

    @Id
    @Column(name = "otherNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String otherDescription;
    private String usageFrequency;
}
