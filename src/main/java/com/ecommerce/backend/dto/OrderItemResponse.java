package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long orderItemId;

    private Long productId;
    private String productName;

    private Integer quantity;

    private Double priceAtPurchase;
    private Double discount;

    private Double totalItemPrice;
}
