package com.example.demo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderFilterDto {
    private Long id;
    private String name;
    private String phone;
    private Integer status;
    private Integer page;
}
