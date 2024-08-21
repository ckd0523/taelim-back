package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "software")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class demand {

    @Id
    @Column(name = "InfoNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InfoNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandBy")
    private member demandBy;

    private LocalDate demandDate;
    private String demandReason;
    private String demandDetail;
    private String disposeMethod;
    private String disposeLocation;
    private String comment;

}
