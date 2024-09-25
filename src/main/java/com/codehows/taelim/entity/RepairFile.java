package com.codehows.taelim.entity;

import com.codehows.taelim.constant.RepairType;
import jakarta.persistence.*;
import lombok.*;

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

}
