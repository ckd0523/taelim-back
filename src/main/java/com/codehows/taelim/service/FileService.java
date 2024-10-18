package com.codehows.taelim.service;

import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {
    File upload(MultipartFile file, CommonAsset commonAsset, FileType fileType);
    Resource getImage(String fileName);
    // 파일 이름으로 파일 정보 조회
    Optional<File> getFileByFileName(String fileName);  // 새로운 메서드 추가

    Optional<File> getFileByFileName1(String fileName);


}
