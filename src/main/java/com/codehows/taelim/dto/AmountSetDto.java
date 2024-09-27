package com.codehows.taelim.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AmountSetDto {
    private Long high_value_standard;
    private Long low_value_standard;

    public AmountSetDto(Long high_value_standard, Long low_value_standard) {
        this.high_value_standard = high_value_standard;
        this.low_value_standard = low_value_standard;
    }
}
