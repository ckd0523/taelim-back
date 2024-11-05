package com.codehows.taelim.dto;


import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.entity.CommonAsset;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {

    private Long assetNo;
    private Long fileNo;
    private String oriFileName;
    private String fileName;
    private Long fileSize;
    private String fileExt;
    private String fileURL;

    private FileType fileType;

}
