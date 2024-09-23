package com.codehows.taelim.dto;


import com.codehows.taelim.constant.FileType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {

    private Long fileNo;
    private String oriFileName;
    private String fileName;
    private Long fileSize;
    private String fileExt;
    private String fileURL;

    private FileType fileType;

}
