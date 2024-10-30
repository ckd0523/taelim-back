package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.ProductDto;
import com.codehows.taelim.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crawl")
public class CrawlerController {

    private final CrawlingService crawlingService;


    @GetMapping("/products")
    public ResponseEntity<Map<String,Object>> getProducts(@RequestParam String keyword) {
        Map<String, Object> productDTOList =crawlingService.scraperProductData(keyword);
        System.out.println(productDTOList);
        return ResponseEntity.ok(productDTOList);
    }
}

