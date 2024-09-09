package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationProgramDto {
    private Long appNo;

    private CommonAsset assetNo;

    private String serviceScope;
    private String os;
    private String relatedDB;
    private String ip;
    private Long screenNumber;
}
