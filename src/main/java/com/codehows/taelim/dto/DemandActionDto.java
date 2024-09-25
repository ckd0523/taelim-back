package com.codehows.taelim.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DemandActionDto {

    private DemandHistoryDto demandAction;
    private String reason;
    private String actionType;
}
