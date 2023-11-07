package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.request.CreateOrderReq;
import com.example.demo.model.request.UpdateOrderReq;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders(); // Lấy ra danh sach orders
    Optional<Order> getOrderById(long id); // Lấy order theo id
    Order createOrder(CreateOrderReq req, User user); // Tạo thêm order
    void deleteOrder(long id); // Xóa order

    List<Order> getOrderByUser (User user);

    List<OrderDto> getOrderHistoryByUser(User user);

    Page<Order> getFilterOrders(Long orderId,String receiverName, String receiverPhone, Integer status, Integer page);

    Order updateOrder(Long id, UpdateOrderReq rq);


}
