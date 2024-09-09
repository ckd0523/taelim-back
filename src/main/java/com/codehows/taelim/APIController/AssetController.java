package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.dto.ExcelDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import com.codehows.taelim.service.FileService;
import com.codehows.taelim.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/asset")
public class AssetController {

    private final RegisterService registerService;
    private final FileService fileService;


    @GetMapping("/get")
    public ResponseEntity<List<AssetDto>> getAllAssets() {
        List<AssetDto> assets = registerService.findAll();
        return ResponseEntity.ok(assets);

    }

    @PostMapping("/register")
    public ResponseEntity<Long> registerAsset(@RequestBody AssetDto assetDto) {

        System.out.println(assetDto.getAssetClassification()+"여기");
        Long assetNo = registerService.assetRegister(assetDto);

        return new ResponseEntity<>(assetNo, HttpStatus.OK);

    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("assetNo") String assetId, @RequestParam("fileType") String fileType) {

        System.out.println("Received assetId : "+ assetId);
        Long assetNo = Long.valueOf(assetId);
        System.out.println("Received assetId : "+ assetNo);
        CommonAsset asset = registerService.findById(assetNo).orElseThrow(() -> new RuntimeException("자산을 찾을 수 없습니다."));

        FileType type;
        try{
            type = FileType.valueOf(fileType);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>("잘못된 파일 유형입니다", HttpStatus.BAD_REQUEST);
        }
        System.out.println("Found asset: " + asset);
        File savedFile = fileService.upload(file, asset, type);
        if (savedFile != null) {
            return new ResponseEntity<>("파일이 성공적으로 업로드되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("파일 업로드에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/excelRegister")
    public ResponseEntity<?> uploadExcelData(@RequestBody List<ExcelDto> excelDtos) {
        try{
            System.out.println("Received excel : "+ excelDtos);

//            return new ResponseEntity<>( , HttpStatus.OK);
        }catch (Exception e) {
//            return new ResponseEntity<>("엑셀 데이터 업로드 실패 " , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        registerService.excelRegisterAll(excelDtos);

        return new ResponseEntity<>("엑셀데이터 정상적으로 업로드 " ,HttpStatus.OK);
    }



}
