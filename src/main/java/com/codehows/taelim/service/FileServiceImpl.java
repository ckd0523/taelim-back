package com.codehows.taelim.service;


import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codehows.taelim.entity.File;

import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${file.url}")
    private String fileUrl;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    //이미지 업로드
    @Override
    public File upload(MultipartFile file,CommonAsset commonAsset, FileType fileType) {
        //빈파일이라면 null
        if(file.isEmpty()) {
            System.out.println("Empty file.");
            return null;
        }

        String originalFileName = file.getOriginalFilename();

        //확장자 가져오기
        String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + extension;
        String savePath = filePath + saveFileName;

        try {
            java.io.File dir = new java.io.File(filePath);
            if(!dir.exists()){
                dir.mkdirs();
            }
            file.transferTo(new java.io.File(savePath));
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        String url = fileUrl + saveFileName;

        File toFile = File.builder()
                .assetNo(commonAsset)
                .oriFileName(originalFileName)
                .fileName(saveFileName)
                .fileSize(file.getSize())
                .fileExt(extension)
                .fileURL(url)
                .fileType(fileType)
                .build();

        return fileRepository.save(toFile);
    }

//    @Override
//    public Resource getImage(String fileName) {
//
//        Resource resource = null;
//
//        try{
//            resource = new UrlResource("file:" + filePath + fileName);
//        }catch (Exception exception) {
//            exception.printStackTrace();
//            return null;
//        }
//        return resource;
//    }
@Override
public Resource getImage(String fileName) {
    Resource resource = null;

    // fileName을 사용하여 파일 정보를 데이터베이스에서 조회 (Optional 사용)
    Optional<File> optionalFile = fileRepository.findByFileName(fileName);

    // 파일이 존재하지 않을 경우 예외 처리
    File file = optionalFile.orElseThrow(() ->
            new RuntimeException("File not found with fileName: " + fileName)
    );

    // 파일 경로 생성
    String fullPath = filePath + file.getFileName();
    // Java의 File 클래스를 사용하여 물리적인 파일 객체를 생성
    java.io.File physicalFile = new java.io.File(fullPath);

    // 실제 파일이 존재하는지 확인
    if (!physicalFile.exists()) {
        throw new RuntimeException("File not found on disk: " + fullPath);
    }

    // UrlResource 생성
    try {
        resource = new UrlResource("file:" + fullPath);
    } catch (Exception exception) {
        exception.printStackTrace();
        return null;
    }

    return resource;
    }
    @Override
    public Optional<File> getFileByFileName(String fileName) {
        return fileRepository.findByFileName(fileName);  // Repository에서 파일 조회
    }
}
