package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    PostRepository postRepository;

    @Override
    public Page<Post> getListPost(Integer pageNumber, Integer pageSize, String sort) {
        // tạo 1 pageable object
        Pageable pageable = null;

        if (sort != null) {
            // COs sort
            Sort sorting = Sort.by(Sort.Direction.ASC, sort);
            pageable = PageRequest.of(pageNumber, pageSize, sorting);
        }else {
            // Neu khong ocos sort
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        // Tra ve page
        return postRepository.findAll(pageable);
    }
}
