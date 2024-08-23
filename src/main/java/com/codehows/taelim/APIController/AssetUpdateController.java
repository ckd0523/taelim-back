package com.codehows.taelim.APIController;


import com.codehows.taelim.dto.UpdateDto;
import com.codehows.taelim.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AssetUpdateController {

    private final UpdateService updateService;


    @PostMapping("/insert")
    public ResponseEntity<?> insertAsset(@RequestBody UpdateDto updateDto) {
        updateService.update(updateDto);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }
}
