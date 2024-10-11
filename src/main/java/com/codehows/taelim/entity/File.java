package com.codehows.taelim.entity;

import com.codehows.taelim.constant.FileType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
    private Long fileSize;
    private String fileExt;
    private String fileURL;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    // 기존의 생성자 및 메서드들...
}
