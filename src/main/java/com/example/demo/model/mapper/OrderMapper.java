package com.example.demo.model.mapper;

import com.example.demo.entity.Order;
import com.example.demo.model.dto.OrderDto;
import lombok.Data;

@Data
public class OrderMapper {
    public OrderDto orderDto (Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setReceiverName(order.getReceiverName());
        orderDto.setReceiverPhone(order.getReceiverPhone());
        orderDto.setReceiverAddress(order.getReceiverAddress());


        return orderDto;
    }
}
