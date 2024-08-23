package com.codehows.taelim.entity;

import com.codehows.taelim.constant.FileType;
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
public class File {
    @Id
    @Column(name = "fileNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetNo")
    private CommonAsset assetNo;

    private String oriFileName;
    private String fileName;
    private String fileSize;
    private String fileExt;
    private String fileURL;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
}
