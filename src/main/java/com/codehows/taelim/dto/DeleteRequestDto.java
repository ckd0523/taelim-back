package com.codehows.taelim.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeleteRequestDto {
    //프론트에서 자산 조사 삭제를 할 때 사용하는 dto
    private List<Integer> assetSurveyNo;
}
