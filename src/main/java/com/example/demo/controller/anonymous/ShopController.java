package com.example.demo.controller.anonymous;

import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
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

    /**
     * Đang làm zở
     * @param model
     * @param slug
     * @param id
     * @return
     */
    @GetMapping("/san-pham/{slug}/{id}")
    public String getProductByIdAndSlug(
            Model model,
            @PathVariable("slug") String slug,
            @PathVariable("id") String id
    ){
        try {
            // Lấy product theo id
            Optional<ProductDto> productDtoOptional = productService.getProductById(id);
            if (productDtoOptional.isPresent()){
                //Add vào model
                model.addAttribute("product",productDtoOptional.get());

                // lấy số tổng số lượng sản phẩm
                int productQuatity = productService.getQuantityByProductId(id);

                // Nếu tổng số lượng > số lượng đã baán => vẫn còn
                model.addAttribute("canBuy", productQuatity > productDtoOptional.get().getTotalSold());
            }
            return "/shop/detail";
        }catch (Exception e){
            return "Lỗi";
        }
    }

}

