package com.example.demo.model.dto;

import com.example.demo.entity.Product;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Product product;
    private int size;
    private long productPrice;
    private long totalPrice;

}
