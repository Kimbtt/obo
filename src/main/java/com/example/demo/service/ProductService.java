package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSize;
import com.example.demo.model.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<ProductDto> getProductDtoById(String Id);

    Optional<Product> getProductById(String Id);
//     Product getProductById(String Id);

    List<ProductDto> getListNewProduct();

    Page<Product> getListProduct(Integer page);

    List<Product> getAllProducts();

    boolean isExistProductSize(String id);

    String uploadImg(MultipartFile file);
}
