package com.codehows.taelim.APIController;

import com.codehows.taelim.dto.ProductDto;
import com.codehows.taelim.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crawl")
public class CrawlerController {

    private final CrawlingService crawlingService;


    @GetMapping("/products")
    public List<ProductDto> getProducts() {
        List<ProductDto> productDTOList =crawlingService.scraperProductData();
        System.out.println(productDTOList);
        return productDTOList;
    }
}

