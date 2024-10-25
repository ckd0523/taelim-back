package com.codehows.taelim.APIController;


import com.codehows.taelim.entity.File;
import com.codehows.taelim.service.FileService;
import com.codehows.taelim.service.RepairFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) {
//        Resource resource = fileService.getImage(fileName);
//        return resource;
        // 데이터베이스에서 fileName으로 파일 정보 조회
        // Optional에서 File 추출
//        File file = fileService.getFileByFileName(fileName)
//                .orElseThrow(() -> new RuntimeException("File not found with fileName:" + fileName));
        File file = fileService.getFileByFileName1(fileName).orElseThrow();


        if (file == null) {
            throw new RuntimeException("File not found with fileName:" + fileName);
        }
        // 원본 파일 이름을 가져옴
        String oriFileName = file.getOriFileName();

        // 서버에서 실제 파일 가져오기
        Resource resource = fileService.getImage(fileName);

        if (resource == null) {
            throw new RuntimeException("File not found on server: " + fileName);
        }

        // MIME 타입 결정
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM; // 기본값
        String disposition = "attachment; filename=\"" + oriFileName  + "\""; // 파일 이름 설정

        // 파일의 MIME 타입 결정
        if (oriFileName .endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (oriFileName .endsWith(".jpeg") || oriFileName .endsWith(".jpg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (oriFileName .endsWith(".txt")) {
            mediaType = MediaType.TEXT_PLAIN;
        } else if (oriFileName .endsWith(".hwp")) {
            mediaType = MediaType.parseMediaType("application/x-hwp"); // HWP 파일에 대한 MIME 타입
        }

        // 파일 이름을 UTF-8로 인코딩
        String encodedFileName = UriUtils.encode(oriFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition ) // Content-Disposition 헤더 추가
                .body(resource);
    }
}
