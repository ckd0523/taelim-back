package com.codehows.taelim.APIController;


import com.codehows.taelim.constant.RepairStatus;
import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.dto.RepairDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import com.codehows.taelim.service.RepairFileService;
import com.codehows.taelim.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintain")
public class MaintainController {

    private final RepairService repairService;
    private final RepairFileService fileService;

    @PostMapping("/register")
    public ResponseEntity<?> maintainRegister(@RequestBody RepairDto repairDto) {

        Long repairNo = repairService.repairRegister(repairDto);
        System.out.println("registerRepairNo : "+repairNo);

        return new ResponseEntity<>(repairNo, HttpStatus.OK);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("repairNo") Long repairNo, @RequestParam MultipartFile file,   @RequestParam("repairType") String repairType){
        System.out.println("Received repairNo: " + repairNo);  // repairNo 로그 추가
        RepairHistory repairHistoryNo = repairService.findById(repairNo).orElseThrow(()->new RuntimeException("유지보수 번호를 찾을 수 없습니다."));
        RepairType type;
        try{
            type = RepairType.valueOf(repairType);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>("잘못된 파일 유형입니다.", HttpStatus.BAD_REQUEST);
        }
        RepairFile savedFile = fileService.upload(file, repairHistoryNo,type);
        if(savedFile != null) {
            return new ResponseEntity<>(savedFile.getFileName(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("파일 업로드에 실패함", HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/file/upload/{repairNo}")
    public ResponseEntity<String> uploadFile2(@PathVariable("repairNo") Long repairNo, @RequestParam MultipartFile file,   @RequestParam("repairType") String repairType){

        RepairHistory repairHistoryNo = repairService.findById(repairNo).orElseThrow(()->new RuntimeException("유지보수 번호를 찾을 수 없습니다."));
        RepairType type;

        try{
            type = RepairType.valueOf(repairType);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>("잘못된 파일 유형입니다.", HttpStatus.BAD_REQUEST);
        }
        RepairFile savedFile = fileService.upload(file, repairHistoryNo,type);


        if(savedFile != null) {
            return new ResponseEntity<>(savedFile.getFileName(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("파일 업로드에 실패함", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/update/{repairNo}")
    public ResponseEntity<?> updateRepair(@PathVariable Long repairNo, @RequestBody RepairDto repairDto){


        RepairHistory repairHistory = repairService.findById(repairNo).orElseThrow(()-> new RuntimeException("해당 유지보수 내역이 존재하지 않음"));

                repairHistory.setRepairStartDate(repairDto.getRepairStartDate());
                repairHistory.setRepairEndDate(repairDto.getRepairEndDate());
                repairHistory.setRepairResult(repairDto.getRepairResult());
                repairHistory.setRepairStatus(repairDto.getRepairStatus());
                if(repairDto.getAssetNo() != null) {
                    CommonAsset assetNo = new CommonAsset();
                    assetNo.setAssetNo(repairDto.getAssetNo());
                    repairHistory.setAssetNo(assetNo);
                }

        repairService.save(repairHistory);

        return new ResponseEntity<>(repairNo, HttpStatus.OK);
    }


    @GetMapping(value = "{fileName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public Resource getImage(@PathVariable("fileName") String fileName) {
        Resource resource;
        resource = fileService.getImage(fileName);
        return resource;
    }


    @GetMapping("/get")
    public ResponseEntity<List<RepairDto>> getAllRepairs() {

        List<RepairDto> allRepairs = repairService.findAll();


        return new ResponseEntity<>(allRepairs, HttpStatus.OK);
    }
}
