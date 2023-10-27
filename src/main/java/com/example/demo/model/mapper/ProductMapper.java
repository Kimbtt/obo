package com.example.demo.model.mapper;

import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;

public class ProductMapper {
    public static ProductDto toProductDto (Product product) {
        ProductDto rs = new ProductDto();
        rs.setId(product.getId());
        rs.setName(product.getName());
        rs.setSlug(product.getSlug());
        rs.setPrice(product.getPrice());
        rs.setTotalSold(product.getTotalSold());
        rs.setProductImages(product.getProductImages());
        rs.setTotalSold(product.getTotalSold());
        return rs;
    }
}
