package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.mapper.OrderMapper;
import com.example.demo.model.request.CreateOrderReq;
import com.example.demo.model.request.UpdateOrderReq;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static com.example.demo.config.Constant.ADMIN_LIMIT_POST_PER_PAGE;

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

        // Kiểm tra productOptional có pần tử không => có => thực thi lưu order
        if (productOptional.isPresent()) {
            // Tạo 1 đối tượng order
            Order order = Order.builder()
                    .product(productOptional.get())
                    .receiverAddress(req.getReceiverAddress())
                    .receiverName(req.getReceiverName())
                    .receiverPhone(req.getReceiverPhone())
                    .productPrice(req.getProductPrice())
                    .totalPrice(req.getTotalPrice())
                    .size(req.getSize())
                    .buyer(user)
                    .createdBy(user)
                    .createdAt(now)
                    .status(1)
                    .build();
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

    @Override
    public Page<Order> getFilterOrders(Long orderId, String receiverName, String receiverPhone, Integer status,
                                       Integer page) {

        Pageable pageable = PageRequest.of(page, ADMIN_LIMIT_POST_PER_PAGE);

        return orderRepository.findFilteredOrders(orderId, receiverName, receiverPhone, status, pageable);

    }

    @Override
    public Order updateOrder(Long id, UpdateOrderReq rq){
        // Lấy order
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            // Cập nhật order
            order.setStatus(rq.getStatus());

            // Lưu vào db
            return orderRepository.save(order);
        }else {
            throw new NotFoundException("Không tìm thấy đơn hàng với id: " + id);
        }
    }
}
