package com.example.demo.service;

import com.example.demo.entity.Brand;
import com.example.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BrandServiceImpl implements BrandService{
    private final BrandRepository brandRepository;
    @Override
    public List<Brand> getListBrand() throws Exception{
        try {
            List<Brand> brands = brandRepository.findAll();
            return brands;
        } catch (Exception e){
            throw new Exception("Không lấy được danh sách brand" + e.getMessage());
        }
    }
}
