package com.codehows.taelim.APIController;


import com.codehows.taelim.dto.AssetUpdateDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AssetUpdateController {

    private final UpdateService updateService;


    @PostMapping("/insert")
    public ResponseEntity<?> insertAsset(@RequestBody AssetUpdateDto updateDto) {
        updateService.update(updateDto);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    // 전체 자산 목록 조회
    @GetMapping("/assets")
    public ResponseEntity<List<CommonAsset>> getAllAssets() {
        List<CommonAsset> assets = updateService.findAll();
        return ResponseEntity.ok(assets);
    }
}
