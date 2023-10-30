package com.example.demo.model.mapper;

import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;

public class ProductMapper {
    public static ProductDto toProductDto (Product product) {
        ProductDto tmp = new ProductDto();
        tmp.setId(product.getId());
        tmp.setName(product.getName());
        tmp.setSlug(product.getSlug());
        tmp.setPrice(product.getPrice());
        tmp.setTotalSold(product.getTotalSold());
        tmp.setImage(product.getProductImages().get(0));
        tmp.setIsAvailable(product.getIsAvailable());
        tmp.setDescription(product.getDescription());
        tmp.setOnfeetImages(product.getOnfeetImages());
        tmp.setBrandId(product.getBrandId());
        return tmp;
    }
}
