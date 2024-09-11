package com.codehows.taelim.dto;

import com.codehows.taelim.entity.Demand;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssetDisposeDto {

    private Long assetDisposeNo;
    private String assetCode;
    private String assetName;
    private String disposeUser;
    private String disposeReason;
    private String disposeDetail;
    private String disposeLocation;
    private String disposeMethod;
    private LocalDate disposeDate;
}
