package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {
    private Long orderId;
    private String customerEmail;
    private LocalDateTime orderedAt;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;
    private List<OrderItemResponse> items;
}
