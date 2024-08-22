package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "repairHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RepairHistory {
    @Id
    @Column(name = "repairNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private LocalDate repairStartDate;
    private LocalDate repairEndDate;
    private String repairBy;
    private String repairResult;
    private Boolean repairStatus;

}
