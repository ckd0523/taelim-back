package com.codehows.taelim.APIController;

import com.codehows.taelim.constant.FileType;
import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.File;
import com.codehows.taelim.service.AssetService;
import com.codehows.taelim.service.FileService;
import com.codehows.taelim.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

//        File url = fileService.upload(file, assetDto);
//        System.out.println("File uploaded successfully : "  + url );
        return new ResponseEntity<>(assetNo, HttpStatus.OK);

    }

    // 자산 수정등록
    @PostMapping("/update/{assetCode}")
    public ResponseEntity<String> updateAsset(
            @PathVariable String assetCode,
            @RequestBody AssetDto updatedAssetDto) {

        Long newAssetNo = registerService.updateAssetCode(assetCode, updatedAssetDto);

        return ResponseEntity.ok("New asset registered with assetNo: " + newAssetNo);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("assetNo") String assetId) {

        System.out.println("Received assetId : "+ assetId);
        Long assetNo = Long.valueOf(assetId);
        System.out.println("Received assetId : "+ assetNo);
        CommonAsset asset = registerService.findById(assetNo).orElseThrow(() -> new RuntimeException("자산을 찾을 수 없습니다."));

        System.out.println("Found asset: " + asset);
        File savedFile = fileService.upload(file, asset, FileType.PHOTO);
        if (savedFile != null) {
            return new ResponseEntity<>("파일이 성공적으로 업로드되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("파일 업로드에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
