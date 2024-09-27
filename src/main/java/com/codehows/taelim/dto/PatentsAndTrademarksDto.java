package com.codehows.taelim.dto;

import com.codehows.taelim.constant.CountryApplication;
import com.codehows.taelim.constant.PatentClassification;
import com.codehows.taelim.constant.PatentItem;
import com.codehows.taelim.constant.PatentTrademarkStatus;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.PatentsAndTrademarks;
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

    // DTO에서 엔티티로 변환
    public PatentsAndTrademarks toEntity() {
        return PatentsAndTrademarks.builder()
                .patentTrademarkNo(this.patentTrademarkNo)
                .assetNo(this.assetNo)
                .applicationDate(this.applicationDate)
                .registrationDate(this.registrationDate)
                .expirationDate(this.expirationDate)
                .patentTrademarkStatus(this.patentTrademarkStatus)
                .countryApplication(this.countryApplication)
                .patentClassification(this.patentClassification)
                .patentItem(this.patentItem)
                .applicationNo(this.applicationNo)
                .inventor(this.inventor)
                .assignee(this.assignee)
                .relatedDocuments(this.relatedDocuments)
                .build();
    }

    // 엔티티에서 DTO로 변환
    public static PatentsAndTrademarksDto fromEntity(PatentsAndTrademarks entity) {
        return new PatentsAndTrademarksDto(
                entity.getPatentTrademarkNo(),
                entity.getAssetNo(),
                entity.getApplicationDate(),
                entity.getRegistrationDate(),
                entity.getExpirationDate(),
                entity.getPatentTrademarkStatus(),
                entity.getCountryApplication(),
                entity.getPatentClassification(),
                entity.getPatentItem(),
                entity.getApplicationNo(),
                entity.getInventor(),
                entity.getAssignee(),
                entity.getRelatedDocuments()
        );
    }
}
