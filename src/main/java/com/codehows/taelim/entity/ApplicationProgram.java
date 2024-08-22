package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "applicationProgram")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationProgram {
    @Id
    @Column(name = "appNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String serviceScope;
    private String OS;
    private String relatedDB;
    private String IP;
    private Long screenNumber;
}
