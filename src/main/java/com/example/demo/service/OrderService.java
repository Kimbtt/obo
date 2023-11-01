package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.model.request.CreateOrderReq;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders(); // Lấy ra danh sach orders
    Optional<Order> getOrderById(long id); // Lấy order theo id
    Order createOrder(CreateOrderReq req, User user); // Tạo thêm order
    void deleteOrder(long id); // Xóa order

    List<Order> getOrderByUser (User user);



}
