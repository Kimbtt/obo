package com.example.demo.service;

import com.example.demo.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders(); // Lấy ra danh sach orders
    Optional<Order> getOrderById(long id); // Lấy order theo id
    Order createOrder(Order order); // Tạo thêm order
    void deleteOrder(long id); // Xóa order
}
