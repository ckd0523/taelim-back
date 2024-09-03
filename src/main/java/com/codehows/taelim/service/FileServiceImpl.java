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

    @Override
    public Resource getImage(String fileName) {

        Resource resource = null;

        try{
            resource = new UrlResource("file:" + filePath + fileName);
        }catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return resource;
    }
}
