package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSize;
import com.example.demo.model.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Optional<ProductDto> getProductById(String Id);
//    public Product getProductById(String Id);

    public List<ProductDto> getListNewProduct();

    public Page<Product> getListProduct(Integer page);

    public boolean isExistProductSize(String id);
}
