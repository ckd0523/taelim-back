package com.codehows.taelim.service;


import com.codehows.taelim.dto.ProductDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CrawlingService {
    private static final Map<String, String> CATEGORY_URL_MAP = new HashMap<>();

    static {
        CATEGORY_URL_MAP.put("노트북" , "https://prod.danawa.com/list/?cate=112758");
        CATEGORY_URL_MAP.put("책상", "https://prod.danawa.com/list/?cate=15240504");
    }
    public Map<String, Object> scraperProductData(String keyword) {
        List<ProductDto> productDTOList = new ArrayList<>();
//        String url = CATEGORY_URL_MAP.get(keyword);
        String url = findClosestUrl(keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("sourceUrl" , url !=null ? url : "No URL mapped for keyword");

        if(url == null) {
            System.out.println("No URL mapped for keyword: " + keyword);
            result.put("products" , productDTOList);
            return result;
        }
        try {
//            String url = "https://prod.danawa.com/list/?cate=112758";
//            String url = "https://prod.danawa.com/list/?cate=15237574";
            Document doc = Jsoup.connect(url).get();
            System.out.println("Page title : " + doc.title());

            // Select product elements with the `prod_item` class
            Elements productElements = doc.select("ul.product_list li.prod_item");
            System.out.println("productElements size : " + productElements.size());

            productElements.forEach(productElement -> {

                Element productTitleElement = productElement.selectFirst("div.prod_main_info div.prod_info p.prod_name a");
                String productTitle = productTitleElement != null ? productTitleElement.text() : "No title";

                Element productPriceElement = productElement.selectFirst("div.prod_main_info div.prod_pricelist ul li.rank_one p.price_sect strong");
                String productPrice = productPriceElement != null ? productPriceElement.text() : "No price";

                if(productPrice.equals("No price")) {
                    productPriceElement = productElement.selectFirst("div.prod_main_info div.prod_pricelist div.box__mall-type a.link__mall-type div.box__price div.sell-price span.text__number");
                    productPrice = productPriceElement != null ? productPriceElement.text() : "No price";
                    if(productPrice.equals("No price")) {
                        productPriceElement = productElement.selectFirst("div.prod_main_info div.prod_pricelist p.price_sect strong");
                        productPrice = productPriceElement != null ? productPriceElement.text() : "No price";
                    }
                }
                Element productMemoryElement = productElement.selectFirst("div.prod_main_info p.memory_sect");
                String productMemory = productMemoryElement != null ? productMemoryElement.text() : "No memory info";


                Element productImageElement = productElement.selectFirst("div.prod_main_info .thumb_image img");
                String image = productImageElement != null ? productImageElement.attr("src") : "No image";

                if (image.equals("No image") || image.isEmpty()) {
                    image = productImageElement != null ? productImageElement.attr("data-original") : "No image";
                }

                if (!image.startsWith("http") && !image.equals("No image")) {
                    image = "https:" + image;
                }


                System.out.println("Product Title : " + productTitle);
                System.out.println("Product Price : " + productPrice);
                System.out.println("Product Image: " + image);
                productDTOList.add(new ProductDto(productTitle, productPrice,image));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("products", productDTOList);
        return result;
    }

    private String findClosestUrl(String keyword) {
        if(CATEGORY_URL_MAP.containsKey(keyword)){
            return CATEGORY_URL_MAP.get(keyword);
        }
        for(Map.Entry<String,String > entry : CATEGORY_URL_MAP.entrySet()) {
            if(keyword.contains(entry.getKey())){
                return entry.getValue();
            }
        }
        return null;
    }
}
