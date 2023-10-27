package com.example.demo.service;

import com.example.demo.model.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface ProductService {
    public Optional<ProductDto> getProductById(String Id);
}
