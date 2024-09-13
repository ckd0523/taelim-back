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
public class OtherAssetsDto {
    private Long otherNo;

    private CommonAsset assetNo;

    private String otherDescription;
    private String usageFrequency;
}
