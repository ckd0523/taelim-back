package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.entity.CommonAsset;
import com.codehows.taelim.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/asset")
public class AssetController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public void registerAsset(@RequestBody AssetDto assetDto){

        registerService.assetRegister(assetDto);

    }
    @GetMapping("/get")
    public ResponseEntity<List<AssetDto>> getAllAssets() {
        List<AssetDto> assets = registerService.findAll();
        return ResponseEntity.ok(assets);
    }
}
