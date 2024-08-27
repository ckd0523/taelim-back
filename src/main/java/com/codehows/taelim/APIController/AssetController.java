package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/asset")
public class AssetController {

    private final RegisterService registerService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerAsset(@RequestBody AssetDto assetDto){

        System.out.println(assetDto.getAssetClassification()+"여기");
        registerService.assetRegister(assetDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
