package com.codehows.taelim.APIController;


import com.codehows.taelim.constant.RepairType;
import com.codehows.taelim.dto.RepairDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.entity.RepairFile;
import com.codehows.taelim.entity.RepairHistory;
import com.codehows.taelim.service.RegisterService;
import com.codehows.taelim.service.RepairFileService;
import com.codehows.taelim.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintain")
public class MaintainController {

    private final RepairService repairService;
    private final RepairFileService fileService;

    @PostMapping("/register")
    public ResponseEntity<?> maintainRegister(@RequestBody RepairDto repairDto) {

        Long assetNo = repairService.repairRegister(repairDto);
        System.out.println(assetNo);

        return new ResponseEntity<>(assetNo, HttpStatus.OK);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file, @RequestParam("repairNo") Long repairNo, @RequestParam("repairType") String repairType){

        RepairHistory repairHistoryNo = repairService.findById(repairNo).orElseThrow(()->new RuntimeException("유지보수 번호를 찾을 수 없습니다."));
        RepairType type;
        try{
            type = RepairType.valueOf(repairType);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>("잘못된 파일 유형입니다.", HttpStatus.BAD_REQUEST);
        }
        RepairFile savedFile = fileService.upload(file, repairHistoryNo, type);
        if(savedFile != null) {
            return new ResponseEntity<>("파일이 성공적으로 업로드 됨", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("파일 업로드에 실패함", HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/get")
    public ResponseEntity<List<RepairDto>> getAllRepairs() {

        List<RepairDto> allRepairs = repairService.findAll();

        return new ResponseEntity<>(allRepairs, HttpStatus.OK);
    }
}
