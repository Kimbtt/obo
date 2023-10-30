package com.example.demo.service;

import com.example.demo.model.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Optional<ProductDto> getProductById(String Id);
    public List<ProductDto> getListNewProduct();
}
