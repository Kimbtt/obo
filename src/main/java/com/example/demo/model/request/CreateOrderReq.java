package com.example.demo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Check;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
public class CreateOrderReq {
    @NotNull(message = "K có id sản phẩm")
    @JsonProperty("product_id")// Trường khi nhận từ Fe
    private String productId;

    @Min(value = 35)
    @Max(value = 42)
    private int size;

    @NotNull(message = "Bạn cần nhập họ tên người nhận")
    @JsonProperty("receiver_name")
    private String receiverName;

    @Pattern(regexp="(09|01[2|6|8|9])+([0-9]{8})\\b", message = "Điện thoại không hợp lệ")
    @JsonProperty("receiver_phone")
    private String receiverPhone;

    @NotNull(message = "Bạn cần nhập địa chỉ người nhận")
    @JsonProperty("receiver_address")
    private String receiverAddress;

    private String note;

    @Min(value = 0, message = "Đơn giá phải lớn hơn 0")
    @JsonProperty("product_price")
    private long productPrice;

    @Min(value = 0, message = "Tổng giá phải lớn hơn 0")
    @JsonProperty("total_price")
    private long totalPrice;
}
