package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.mapper.OrderMapper;
import com.example.demo.model.request.CreateOrderReq;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(CreateOrderReq req, User user) {
        // Lấy thời gian hiện tại
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        // Tạo 1 đối tượng order
        Order order = new Order();

        //Lấy thông tin product từ produc_id
        String productId = req.getProductId();
        Optional<Product> productOptional = productRepository.findById(productId);

        // Lấy thông tin người tạo + người mua hàng
        // Tạm thời làm trường hợp 2 người là 1 guest,
        //Lấy cookie đã lưu
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentPrincipal = (UserDetails) authentication.getPrincipal();
        // Lấy user
        String userEmail = currentPrincipal.getUsername();


        // Lấy User => lấy ng dùng
//        Optional<User> userOptional = userRepository.findById()


        // Kiểm tra productOptional có pần tử không => có => thực thi lưu order
        if (productOptional.isPresent()) {

            order.setProduct(productOptional.get());
            order.setReceiverName(req.getReceiverName());
            order.setReceiverPhone(req.getReceiverPhone());
            order.setReceiverAddress(req.getReceiverAddress());
            order.setProductPrice(req.getProductPrice());
            order.setTotalPrice(req.getTotalPrice());
            order.setSize(req.getSize());
            order.setBuyer(user);
            order.setCreatedBy(user);
            order.setCreatedAt(now);
            return orderRepository.save(order);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrderByUser(User user) {
        List<Order> listOrder = orderRepository.findAllByBuyer(user);
        return listOrder;
    }

    @Override
    public List<OrderDto> getOrderHistoryByUser(User user) {
//        OrderMapper orderMapper = new OrderMapper();
        List<OrderDto> listOrder = orderRepository.findAllByBuyer(user)
                .stream()
                .map(OrderMapper::toOrderDto)
                .toList();

        return listOrder;
    }
}
