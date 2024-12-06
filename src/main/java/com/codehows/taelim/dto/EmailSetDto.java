package com.codehows.taelim.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailSetDto {
    private Long emailSetNo;
    private String setName;
    private String setEmail;
    private Boolean isSelected;
}
