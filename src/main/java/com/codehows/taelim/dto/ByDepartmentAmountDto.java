package com.codehows.taelim.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
//부서별 자산 개수
public class ByDepartmentAmountDto {

    private long managementPlanningAmount;
    private long managementAmount;
    private long salesAmount;
    private long purchaseAmount;
    private long qualityAmount;
    private long productionAmount;
    private long technologyResearchAmount;
}
