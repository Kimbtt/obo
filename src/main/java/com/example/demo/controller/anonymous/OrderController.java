package com.example.demo.controller.anonymous;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.OrderMapper;
import com.example.demo.model.mapper.ProductMapper;
import com.example.demo.model.request.CreateOrderReq;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static com.example.demo.model.mapper.OrderMapper.*;

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
     *
     * @param order
     * @param result
     * @return
     */
    @PostMapping("/api/order")
    public ResponseEntity<?> creatOrder(
            @Valid @RequestBody CreateOrderReq req,
//            BindingResult result,
            HttpServletResponse response
    ) {
        // Lấy thông tin người dùng
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
        if (!currentUserDetails.isEnabled()) {
            // Xử lý trường hợp khi Principal không phải là User (có thể là anonymous user, null, hoặc đối tượng khác)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        String currentUserEmail = currentUserDetails.getUsername();
        // lấy ra usẻr
        User currentUser = userService.getUserByEmail(currentUserEmail);

        try {
//            if (result.hasErrors()) {
//                // Check form nếu có lỗi thì trả về =>> có thể k cần thiết vì fe đã xử lý rồi
//                List<String> errMess = result.getFieldErrors()
//                        .stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(errMess);
//            }
            // Tạo 1 đối tượng mới
            Order order = orderService.createOrder(req, currentUser);

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * Test gọi tất cả order
     *
     * */
    @GetMapping("api/order/list/csv")
    public void exportCsvOrderList(
//            @RequestParam("email") String email
            HttpServletResponse response

    ) throws IOException {
        // Lấy đối tượng user đang đăng nhập
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        List<Order> listOrders = orderService.getOrderByUser(user).stream().toList();

        // Xuất file csv
        response.setContentType("text/csv");
        String fileName = "order.csv";

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;

        response.setHeader(headerKey, headerValue);
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        // Danh sachs header
        String[] csvHeader = {
                "Buy Date",
                "Receiver Name",
                "Receiver Phone",
                "Receiver Address",
                "Product Price",
        };

        //
        String[] namePapping = {
                "createdAt",
                "receiverName",
                "receiverPhone",
                "receiverAddress",
                "productPrice",
        };

        csvWriter.writeHeader(csvHeader);
        for (Order order1 : listOrders) {
            csvWriter.write(order1, namePapping);
        }

        csvWriter.close();
    }

    @GetMapping("tai-khoan/lich-su-giao-dich")
    public String orderHistory(Model model) {
        try {
            // Lấy đối tượng đăng nhập
            User currentUser =
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            // Lấy danh sách order
            List<OrderDto> listOrders = orderService.getOrderHistoryByUser(currentUser);
//            if (listOrders.isEmpty()){
//                // Return ra trang baáo lỗi
//            }
            model.addAttribute("orders", listOrders);

            return "shop/order_history";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Lấy thông tin order
     * 1. lấy thông tin người dùng
     * 2. lấy thông tin order
     * 3. Kiểm tra
     * Điều kiện: Order có thực thể && order.user === currentUser
     * Nếu không có =>> báo lỗi
     * Nếu có =>> trả về OrderDto
     * <p>
     * <p>
     * Hoặc sử dụng query trong repository
     * Điều kiện
     * - có user là currentUser (param)
     * - có id được truyền vào qua requestParam
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("tai-khoan/lich-su-giao-dich/{id}")
    public String orderDetail(Model model, @PathVariable Long id) {
        try {
            // Lấy đối tượng đăng nhập
            User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal()).getUser();
            // Lấy danh sách order
            Optional<Order> orderDetail = orderService.getOrderById(id);

            if (orderDetail.isPresent() && orderDetail.get().getBuyer().getEmail().equals(currentUser.getEmail())) {
                OrderDto orderDto = OrderMapper.toOrderDto(orderDetail.get());
                model.addAttribute("order", orderDto);
            }
            return "shop/order_detail";
        } catch (Exception e) {
            return e.getMessage();
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
