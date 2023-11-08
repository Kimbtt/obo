package com.example.demo.controller.admin;

import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/products")
@RequiredArgsConstructor
public class AdminProductManagerController {
    private final ProductService productService;

    @GetMapping("")
    public String getAllProduct(Model model) {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            throw new NotFoundException("Không có sản phẩm");
        }
        model.addAttribute("products", products);
        return "admin/product/list";
    }

    @GetMapping("/{id}")
    public String getDetailProduct(@PathVariable String id, Model model) {
        //Lấy product theo id
        Optional<Product> productDetail = productService.getProductById(id);

        if (productDetail.isEmpty()) {
            throw new NotFoundException("Không có dữ liệu của id: " + id);
        }
        model.addAttribute("product", productDetail.get());
        return "admin/product/detail";
    }

    /**
     * upload image
     *
     */
    @PostMapping("/addFile")
    public ResponseEntity<?> uploadImg(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImg = productService.uploadImg(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImg);
    }

}
