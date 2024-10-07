package com.codehows.taelim.dto;


import com.codehows.taelim.constant.FileType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private byte[] fileData; // 실제 파일 데이터
    private MultipartFile multipartFile; // 파일 업로드 처리를 위한 필
}
