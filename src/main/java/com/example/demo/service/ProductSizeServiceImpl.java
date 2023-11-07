package com.example.demo.service;

import com.example.demo.entity.ProductSize;
import com.example.demo.repository.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSizeServiceImpl implements ProductSizeService{
    private final ProductSizeRepository productSizeRepository;
    @Override
    public List<ProductSize> getListProductSizes() {
        return productSizeRepository.findAll();
    }
}
