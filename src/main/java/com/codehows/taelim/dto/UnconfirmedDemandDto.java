package com.codehows.taelim.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnconfirmedDemandDto {

    private List<AssetDto> assetDto;
    private DemandHistoryDto demandHistoryDto;


}
