package com.codehows.taelim.service;

import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File upload(MultipartFile file, CommonAsset commonAsset, FileType fileType);
    Resource getImage(String fileName);
}
