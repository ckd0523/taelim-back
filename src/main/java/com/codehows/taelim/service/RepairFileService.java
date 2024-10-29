package com.codehows.taelim.service;

import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import com.codehows.taelim.repository.RepairFileRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
public class RepairFileService {

    private final RepairFileRepository repairFileRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("http://localhost:8080/maintain/img/")
    private String fileUrl;

    public RepairFileService(RepairFileRepository repairFileRepository) {
        this.repairFileRepository = repairFileRepository;
    }


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
                boolean isMakeFile = dir.mkdirs();
                if(isMakeFile){
                    System.out.println("디렉터리 생성 성공");
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                            .size(800,800)
                                    .outputQuality(0.8)
                                            .toOutputStream(outputStream);

           try(FileOutputStream fos = new FileOutputStream(savePath)){
               outputStream.writeTo(fos);
           }
//            multipartFile.transferTo(new File(savePath));
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



        System.out.println("repairNO:"+toRepairFile.getRepairNo().getRepairNo());
        return repairFileRepository.save(toRepairFile);
    }

    public void delete(RepairFile file) {
        try{
            File deleteFile = new File(filePath + file.getFileName());
            if(deleteFile.exists()) {
                deleteFile.delete();
            }
            repairFileRepository.delete(file);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public RepairFile findByRepairNoAndType(RepairHistory repairHistory, RepairType type) {
        return repairFileRepository.findByRepairNoAndRepairType(repairHistory, type);
    }


    public Resource getImage(String fileName) {
        Resource resource = null;


        try{
            resource = new UrlResource("file:" + filePath + fileName );
        }catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return resource;
    }




}
