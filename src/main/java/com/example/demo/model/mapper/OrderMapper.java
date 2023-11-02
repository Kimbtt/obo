package com.example.demo.model.mapper;

import com.example.demo.entity.Order;
import com.example.demo.model.dto.OrderDto;
import lombok.Data;


public class OrderMapper {
    public static OrderDto toOrderDto (Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setReceiverName(order.getReceiverName());
        orderDto.setReceiverPhone(order.getReceiverPhone());
        orderDto.setReceiverAddress(order.getReceiverAddress());
        orderDto.setProduct(order.getProduct());
        orderDto.setSize(order.getSize());
        orderDto.setProductPrice(order.getProductPrice());
        orderDto.setTotalPrice(order.getTotalPrice());

        return orderDto;
    }
}
