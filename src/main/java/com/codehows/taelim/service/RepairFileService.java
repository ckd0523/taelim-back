package com.codehows.taelim.service;

import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import com.codehows.taelim.repository.RepairFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class RepairFileService {

    private RepairFileRepository repairFileRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${file.url}")
    private String fileUrl;

    public RepairFile upload(MultipartFile file, RepairHistory repairHistory, RepairType repairType) {
        if(file.isEmpty()) {
            System.out.println("Empty file");
            return null;
        }
        String originalFileName = file.getOriginalFilename();

        String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) :"";

        String uuid = UUID.randomUUID().toString();
        String saveFileName =  uuid + extension;
        String savePath= filePath + saveFileName;

        try{
            File dir = new File(filePath);
            if(!dir.exists()){
                dir.mkdirs();
            }
            file.transferTo(new File(savePath));
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
        String url = fileUrl + saveFileName;

        RepairFile toRepairFile = RepairFile.builder()
                .repairNo(repairHistory)
                .oriFileName(originalFileName)
                .fileName(saveFileName)
                .fileURL(url)
                .repairType(repairType)
                .build();

        return repairFileRepository.save(toRepairFile);
    }

    public Resource getImage(String fileName) {
        Resource resource = null;


        try{
            resource = new UrlResource("file: " + filePath );
        }catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return resource;
    }




}
