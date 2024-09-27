package com.codehows.taelim.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllDeleteDto {

    private List<AssetUpdateDto> assetDtos;
    private AssetUpdateDto assetDto;
    private Long assetNo;
    private String disposeMethod;
    private String disposeLocation;
    private String reason;
    private String detail;

}
