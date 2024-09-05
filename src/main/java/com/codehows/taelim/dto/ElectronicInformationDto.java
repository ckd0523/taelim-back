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
public class ElectronicInformationDto {
    private Long eInfoNo;

    private CommonAsset assetNo;

    private String os;
    private String system;
    private String DBType;
}
