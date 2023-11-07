package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyer(User user);

    @Query("SELECT o FROM orders o " +
            "JOIN o.product p " +
            "WHERE (:orderId IS NULL OR o.id = :orderId) " +
            "AND (:receiverName IS NULL OR LOWER(o.receiverName) LIKE %:receiverName%) " +
            "AND (:receiverPhone IS NULL OR o.receiverPhone LIKE %:receiverPhone%) " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<Order> findFilteredOrders(@Param("orderId") Long orderId,
                                   @Param("receiverName") String receiverName,
                                   @Param("receiverPhone") String receiverPhone,
                                   @Param("status") Integer status,
                                   Pageable pageable);
}
