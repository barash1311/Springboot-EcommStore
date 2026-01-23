package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productName;
    private Double price;
    private Double discount;
    private Integer quantity;
    private Double subTotal;
}
