package com.codehows.taelim.entity;

import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.dto.RepairDto;
import com.codehows.taelim.dto.RepairFileDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repairFile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RepairFile {
    @Id
    @Column(name = "repairFileNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairFileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repairNo")
    private RepairHistory repairNo;

    private String oriFileName;
    private String fileName;
    private String fileURL;

    @Enumerated(EnumType.STRING)
    private RepairType repairType;

    public RepairFileDto toRepairFile() {
        return RepairFileDto.builder()
                .repairFileNo(repairFileNo)
                .repairNo(repairNo.getRepairNo())
                .oriFileName(oriFileName)
                .fileName(fileName)
                .fileURL(fileURL)
                .repairType(repairType)
                .build();
    }

}
