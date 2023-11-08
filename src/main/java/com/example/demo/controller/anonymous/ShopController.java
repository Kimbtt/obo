package com.example.demo.controller.anonymous;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.mapper.ProductMapper;
import com.example.demo.service.BrandService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.Constant.Size_VN;

@Controller
@RequiredArgsConstructor
public class ShopController {
    @Autowired
    ProductService productService;

    private final BrandService brandService;
    private final CategoryService categoryService;

    /**
     * ra màn home
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String getIndex(Model model) {
        List<ProductDto> products = productService.getListNewProduct();
        model.addAttribute("newProducts", products);
        return "shop/index";
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        Optional<ProductDto> rs = productService.getProductDtoById(id);
        if (!rs.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong tim thay san pham");
        }
        return ResponseEntity.ok(rs);
    }

    /**
     * @param model hello baby
     * @param slug
     * @param id
     * @return
     */
    @GetMapping("/san-pham/{slug}/{id}")
    public String getProductByIdAndSlug(
            Model model,
            @PathVariable("slug") String slug,
            @PathVariable("id") String id
    ) {
            // Lấy product theo id
            Optional<ProductDto> productDtoOptional = productService.getProductDtoById(id);

            // Nếu productDtoOpt tồn tại
            if (productDtoOptional.isPresent()) {
                //Add vào model
                model.addAttribute("product", productDtoOptional.get());

                // Thay dổi định dạng price => add vào model
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(productDtoOptional.get().getPrice());
                model.addAttribute("productProduct", formattedPrice);

                // Nếu trong bảng ProductSize có id => còn hàng
                model.addAttribute("canBuy", productService.isExistProductSize(id));
                return "/shop/detail";
            }
            return "Lỗi";
    }


    /**
     * Laays danh sach san pham
     *
     * 1. Lấy danh sách thương hiệu (brands - id, name)
     * 2. Lấy danh sách danh mục (categories - id, name)
     * 3. Lấy danh sách size (size)
     * 4.
     *
     * @return
     */
    @GetMapping("/san-pham")
    public String GetAllProduct(Model model, @RequestParam(required = false) Integer page) throws Exception {
        if (page == null) {
            page = 0;
        } else {
            page--;
            if (page < 0) {
                page = 0;
            }
        }

        // Lấy 1 page product
        Page<Product> products = productService.getListProduct(page);

        // Chuyển về dto
        Page<ProductDto> productDto = products.map(ProductMapper::toProductDto);

        // Lay danh sach thương hiệu (Brand)
        List<Brand> brands = brandService.getListBrand();

        // Lay danh sách categories
        List<Category> categories = categoryService.getListCategories();

        // Đẩy ra màn hình
        model.addAttribute("listProduct", productDto.getContent());
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("sizeVn", Size_VN);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", ++page);

        return "shop/shop";
    }

}

