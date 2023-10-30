package com.example.demo.controller.anonymous;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ProductService productService;

    /**
     * ra màn home
     * @param model
     * @return
     */
    @GetMapping("/")
    public String getIndex(Model model) {
        List<ProductDto> products = productService.getListNewProduct();
        model.addAttribute("newProducts",products);

        return "shop/index";
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id){
        try{
            // Lay thong tin product
            Optional<ProductDto> rs = productService.getProductById(id);
            if (rs.get() != null){
                return ResponseEntity.ok(rs);
            }
            return ResponseEntity.badRequest().body("Khong co trong db");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
