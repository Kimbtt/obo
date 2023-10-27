package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public Optional<ProductDto> getProductById(String id) {
        try {
            // Laays produc theo id
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                // convert sang Optional<ProductDto>
                Product product = productOptional.get();
                ProductDto productDto = ProductMapper.toProductDto(product);
                return Optional.of(productDto);
            }else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace(); // in ra looix
            return Optional.empty();
        }
    }
}