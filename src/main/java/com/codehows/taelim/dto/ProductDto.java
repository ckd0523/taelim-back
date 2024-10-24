package com.codehows.taelim.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductDto {
    private String productTitle;
    private String productPrice;
    private String productMemory;
    private String image;

}
