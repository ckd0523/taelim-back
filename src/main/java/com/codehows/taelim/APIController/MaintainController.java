package com.codehows.taelim.APIController;


import com.codehows.taelim.dto.RepairDto;
import com.codehows.taelim.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintain")
public class MaintainController {

    private final RepairService repairService;

    @PostMapping("/register")
    public ResponseEntity<?> maintainRegister(@RequestBody RepairDto repairDto) {

        Long assetNo = repairService.repairRegister(repairDto);
        System.out.println(assetNo);

        return new ResponseEntity<>(assetNo, HttpStatus.OK);
    }

    @PostMapping("/file/upload")


    @GetMapping("/get")
    public ResponseEntity<List<RepairDto>> getAllRepairs() {

        List<RepairDto> allRepairs = repairService.findAll();

        return new ResponseEntity<>(allRepairs, HttpStatus.OK);
    }
}
