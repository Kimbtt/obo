package com.example.demo.controller.anonymous;

import com.example.demo.entity.Order;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.request.CreateOrderReq;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;


    /**
     * Tạo mới order
     * @param order
     * @param result
     * @return
     */
    @PostMapping("/api/order")
    public ResponseEntity<?> creatOrder (
            @Valid @RequestBody CreateOrderReq req,
            BindingResult result
    ){
        try {
            if (result.hasErrors()){
                // Check form nếu có lỗi thì trả về =>> có thể k cần thiết vì fe đã xử lý rồi
                List<String> errMess = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMess);
            }
            // Tạo 1 đối tượng mới
            Order order = orderService.createOrder(req);
            return ResponseEntity.ok(order);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    /**
     * Load trang đặt hàng
     * show ra thông tin đặt hàng
     *
     * @param slug
     * @param id
     * @return
     */
    @GetMapping("/dat-hang/{slug}/{id}")
    public String orderPage(
            @PathVariable("slug") String slug,
            @PathVariable("id") String id,
            Model model
    ) {
        try {
            // Lấy product theo id
            Optional<ProductDto> productDtoOptional = productService.getProductById(id);
            if (productDtoOptional.isPresent()) {
                //Add vào model
                model.addAttribute("product", productDtoOptional.get());

                // Thay dổi định dạng price => add vào model
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(productDtoOptional.get().getPrice());
                model.addAttribute("productPrice", formattedPrice);

                // Nếu trong bảng ProductSize có id => còn hàng
                model.addAttribute("canBuy", productService.isExistProductSize(id));
            }

            return "/shop/payment";
        } catch (Exception e) {
            // Xử lý lỗi sau
            return "Lỗi";
        }
    }


    //    /**
//     * Lấy tất cả các order
//     * @return
//     */
//    @GetMapping("/order")
//    public ResponseEntity<?> getAllOrders() {
//        List<Order> rs = orderService.getAllOrders();
//
//        return ResponseEntity.ok(rs);
//    }
//
//    /**
//     * Lấy order theo Id
//     * @param id
//     * @return
//     */
//    @GetMapping("/order/{id}")
//    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
//        try {
//            Optional<Order> rs = orderService.getOrderById(id);
//            if (rs.get() != null){
//                return ResponseEntity.ok(rs.get());
//            }
//            return ResponseEntity.badRequest().body("K cos trong db");
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//
//    /**
//     * Xóa order theo id
//     * @param id
//     * @return
//     */
//    @DeleteMapping("/order/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//        return ResponseEntity.noContent().build();
//    }

}
