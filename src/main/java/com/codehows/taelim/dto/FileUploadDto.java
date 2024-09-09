package com.codehows.taelim.dto;


import com.codehows.taelim.entity.File;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public class FileUploadDto {
    private MultipartFile file;
}
