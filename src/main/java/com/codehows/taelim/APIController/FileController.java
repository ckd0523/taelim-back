package com.codehows.taelim.APIController;


import com.codehows.taelim.entity.File;
import com.codehows.taelim.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "{fileName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public Resource getImage(@PathVariable("fileName") String fileName) {
        Resource resource = fileService.getImage(fileName);
        return resource;
    }


}
