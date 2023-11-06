package com.example.demo.model.dto;

import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long promotionPrice;

    private String id;

    private String name;

    private String slug;

    private Long price;

    private Integer totalSold;

    private Integer brandId;

    private String image;

    private String description;

    private Boolean isAvailable;

    private ArrayList<String> onfeetImages;
}
