package com.example.demo.controller.anonymous;

import com.example.demo.entity.Order;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * Lấy tất cả các order
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getAllOrders() {
        List<Order> rs = orderService.getAllOrders();

        return ResponseEntity.ok(rs);
    }

    /**
     * Lấy order theo Id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Optional<Order> rs = orderService.getOrderById(id);
            if (rs.get() != null){
                return ResponseEntity.ok(rs.get());
            }
            return ResponseEntity.badRequest().body("K cos trong db");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Tạo mới order
     * @param order
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    /**
     * Xóa order theo id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
