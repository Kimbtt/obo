package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.Constant.LIMIT_POST_PER_PAGE;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

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
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace(); // in ra looix
            return Optional.empty();
        }
    }

    /**
     * @return
     */
    @Override
    public List<ProductDto> getListNewProduct() {
        List<Product> newProducts = productRepository.findAllByOrderByCreatedAtDesc();
        List<ProductDto> result = new ArrayList<>();

        for (Product product : newProducts) {
            result.add(ProductMapper.toProductDto(product));
        }
        return result;
    }

    @Override
    public Page<Product> getListProduct(Integer page) {
        Pageable pageable = PageRequest.of(page, LIMIT_POST_PER_PAGE);

        return productRepository.findAll(pageable);
    }

    /**
     * Đang làm zở
     *
     * @param id
     * @return
     */
    public boolean isExistProductSize(String id) {
        return productSizeRepository.existsByProductId(id);
    }


}
