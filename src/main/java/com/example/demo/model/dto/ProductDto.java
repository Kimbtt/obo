package com.example.demo.model.dto;

import lombok.*;

import java.util.ArrayList;

@Data
public class ProductDto {

    private Long promotionPrice;
    private String id;

    private String name;

    private String slug;

    private long price;

    private int totalSold;

    private int brandId;

    private String image;

    private String description;

    private Boolean isAvailable;

    private ArrayList<String> onfeetImages;
}
