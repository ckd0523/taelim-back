package com.codehows.taelim.dto;

import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.entity.RepairHistory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairFileDto {

    private Long repairFileNo;

    private Long repairNo;

    private String oriFileName;
    private String fileName;
    private String fileURL;

    private RepairType repairType;
}
