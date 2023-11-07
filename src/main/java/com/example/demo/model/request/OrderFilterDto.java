package com.example.demo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@AllArgsConstructor
@Data
public class OrderFilterDto {
    private Long id;
    private String name;
    private String phone;
    private Integer status;
    private Integer page;
    public OrderFilterDto() {
        this.page = 0; // Gán giá trị mặc định cho page là 0
    }
}
