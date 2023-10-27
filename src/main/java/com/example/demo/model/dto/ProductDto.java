package com.example.demo.model.dto;

import lombok.Data;

import java.util.ArrayList;


@Data
public class ProductDto {
    private String id;

    private String name;

    private String slug;

    private long price;

    private int totalSold;

    private ArrayList<String> productImages;
}
