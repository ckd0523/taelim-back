package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "amountSet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class amountSet {
    @Id
    @Column(name = "valueStandardNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long valueStandardNo;

    private Long highValueStandard;
    private Long lowValueStandard;
}
