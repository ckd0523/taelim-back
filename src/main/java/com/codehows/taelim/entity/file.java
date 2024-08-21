package com.codehows.taelim.entity;

import com.codehows.taelim.constant.fileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class file {
    @Id
    @Column(name = "fileNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private commonAsset assetNo;

    private String oriFileName;
    private String fileName;
    private String fileSize;
    private String fileExt;
    private String fileURL;
    private fileType fileType;
}
