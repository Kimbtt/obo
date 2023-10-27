package com.example.demo.controller.anonymous;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ProductService productService;

//    @GetMapping("")
//    public ResponseEntity<?> getLisPost(
//            @RequestParam("page") int page,
//            @RequestParam("limit")int limit
//            ) {
//        Page<s>
//
//        return ResponseEntity.ok(String.format("get all posts, page: %d, limit: %d", page, limit));
//    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id){
        // Lay thong tin product
        Optional<ProductDto> rs = productService.getProductById(id);

        return ResponseEntity.ok(rs);
    }
}
