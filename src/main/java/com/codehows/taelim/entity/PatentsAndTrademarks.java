package com.codehows.taelim.entity;

import com.codehows.taelim.constant.CountryApplication;
import com.codehows.taelim.constant.PatentClassification;
import com.codehows.taelim.constant.PatentItem;
import com.codehows.taelim.constant.PatentTrademarkStatus;
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
public class PatentsAndTrademarks {

    @Id
    @Column(name = "patentTrademarkNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patentTrademarkNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private LocalDate applicationDate;
    private LocalDate registrationDate;
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private PatentTrademarkStatus patentTrademarkStatus;

    @Enumerated(EnumType.STRING)
    private CountryApplication countryApplication;

    @Enumerated(EnumType.STRING)
    private PatentClassification patentClassification;

    @Enumerated(EnumType.STRING)
    private PatentItem patentItem;

    private String applicationNo;
    private String inventor;
    private String assignee;
    private String relatedDocuments;

}
