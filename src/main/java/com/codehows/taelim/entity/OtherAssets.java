package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otherAssets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OtherAssets {

    @Id
    @Column(name = "otherNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String otherDescription;
    private String usageFrequency;
}
