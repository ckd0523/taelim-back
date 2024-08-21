package com.codehows.taelim.entity;

import com.codehows.taelim.constant.repairType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "repairFile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class repairFile {
    @Id
    @Column(name = "repairFileNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairFileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repairNo")
    private repairHistory repairNo;

    private String oriFileName;
    private String fileName;
    private String fileURL;
    private repairType repairType;

}
