package com.codehows.taelim.entity;

import com.codehows.taelim.constant.countryApplication;
import com.codehows.taelim.constant.patentClassification;
import com.codehows.taelim.constant.patentItem;
import com.codehows.taelim.constant.patentTrademarkStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "patentsAndTrademarks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class patentsAndTrademarks {

    @Id
    @Column(name = "patentTrademarkNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patentTrademarkNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private LocalDate applicationDate;
    private LocalDate registrationDate;
    private LocalDate expirationDate;
    private patentTrademarkStatus patentTrademarkStatus;
    private countryApplication countryApplication;
    private patentClassification patentClassification;
    private patentItem patentItem;
    private String applicationNo;
    private String inventor;
    private String assignee;
    private String relatedDocuments;

}
