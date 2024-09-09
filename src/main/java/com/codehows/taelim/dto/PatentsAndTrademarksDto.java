package com.codehows.taelim.dto;

import com.codehows.taelim.constant.CountryApplication;
import com.codehows.taelim.constant.PatentClassification;
import com.codehows.taelim.constant.PatentItem;
import com.codehows.taelim.constant.PatentTrademarkStatus;
import com.codehows.taelim.entity.CommonAsset;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatentsAndTrademarksDto {

    private Long patentTrademarkNo;
    private CommonAsset assetNo;
    private LocalDate applicationDate;
    private LocalDate registrationDate;
    private LocalDate expirationDate;
    private PatentTrademarkStatus patentTrademarkStatus;
    private CountryApplication countryApplication;
    private PatentClassification patentClassification;
    private PatentItem patentItem;
    private String applicationNo;
    private String inventor;
    private String assignee;
    private String relatedDocuments;
}
