package com.example.demo.controller.anonymous;

import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
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
        Optional<ProductDto> rs = productService.getProductById(id);
        if (!rs.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong tim thay san pham");
        }
        return ResponseEntity.ok(rs);
    }

    /**
     *
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

                // Thay dổi định dạng price => add vào model
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(productDtoOptional.get().getPrice());
                model.addAttribute("productProduct", formattedPrice);


                // Nếu trong bảng ProductSize có id => còn hàng
                model.addAttribute("canBuy", productService.isExistProductSize(id));
            }
            return "/shop/detail";
        }catch (Exception e){
            return "Lỗi";
        }
    }

}

