package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getListCategories() throws Exception{
        try {
            List<Category> categories = categoryRepository.findAll();
            return categories;
        }catch (Exception e){
            throw new Exception("Không lấy được đanh sách category" + e.getMessage());
        }
    }
}
