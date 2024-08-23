package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.AssetDto;
import com.codehows.taelim.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("")
public class AssetController {

    private final RegisterService registerService;

    @PostMapping("/asset/register")
    public void registerAsset(@RequestParam AssetDto assetDto){
        registerService.assetRegister(assetDto);
    }
}
