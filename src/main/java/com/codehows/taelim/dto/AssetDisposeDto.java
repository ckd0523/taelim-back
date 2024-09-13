package com.codehows.taelim.dto;

import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.Demand;
import com.codehows.taelim.entity.DemandDtl;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssetDisposeDto {

    private String disposeUser;
    private String disposeReason;
    private String disposeDetail;
    private String disposeLocation;
    private String disposeMethod;
    //private LocalDate disposeDate;
}
