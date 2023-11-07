package com.example.demo.controller.admin;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSize;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.request.UpdateOrderReq;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.demo.config.Constant.Size_VN;

@Controller
@RequiredArgsConstructor
public class AdminOrderManagerController {
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductSizeService getListProductSizes;

    /**
     * @param orderId       Id đơn hàng
     * @param productName   tên sản phẩm
     * @param receiverPhone sdt người nhận
     * @param status        trang thái đơn hàng
     * @param page          số trang
     * @return page
     */
    @GetMapping("admin/orders")
    public String getFilterOrders(
            Model model,
            @RequestParam(value = "id", required = false) Long orderId,
            @RequestParam(value = "name", required = false) String productName,
            @RequestParam(value = "phone", required = false) String receiverPhone,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "1") Integer page
//            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        if (page == null) {
            page = 0;
        } else {
            page--;
            if (page < 0) {
                page = 0;
            }
        }
        // Lấy list orders sau sort
        Page<Order> listFilterOrder = orderService.getFilterOrders(orderId, productName, receiverPhone, status, page);

        if (listFilterOrder == null) {
            // Lay tat ca danh sach order
            List<Order> listOrders = orderService.getAllOrders();
            // Đút vào fe
            model.addAttribute("orders", listOrders);
        } else {
            // Add vào model
            model.addAttribute("orders", listFilterOrder.getContent());
        }

        return "admin/order/list";
    }

    /**
     * @param model model
     * @param id    orderId
     * @return detail
     * @throws Exception
     */
    @GetMapping("admin/orders/{id}")
    public String getOrderById(Model model, @PathVariable long id) throws Exception {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            Order detailOrder = order.get();
            model.addAttribute("order", detailOrder);
            return "admin/order/detail";
        } else {
            throw new Exception("Lỗi load trang");
        }
    }


    // Cập nhật đơn hàng
    @PutMapping("admin/orders/{id}/update-status")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderReq req) {
        Order order = orderService.updateOrder(id, req);
        return (order != null)
                ? ResponseEntity.ok(order)
                : ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy đơn hàng
    }

    //Tạo đơn hàng mới
    @GetMapping("admin/orders/create")
    public String createOrderPage(Model model) {

        //Lấy danh sách sản phẩm
        List<Product> products = productService.getAllProducts();
        if (!products.isEmpty()) {
//            List<String> productNames = products.stream()
//                    .map(Product::getName)
//                    .toList();
            model.addAttribute("products", products);
        }
        // Lấy danh sách size
        List<ProductSize> productSizes = getListProductSizes.getListProductSizes();
        if (!productSizes.isEmpty()) {
            model.addAttribute(productSizes);
        }

        model.addAttribute("sizes", Size_VN);

        return "admin/order/create";
    }

}
