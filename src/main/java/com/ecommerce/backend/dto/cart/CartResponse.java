package com.ecommerce.backend.dto.cart;

import com.ecommerce.backend.dto.cartItem.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CartResponse {
    private Long cartId;
    private Double totalPrice;
    private List<CartItemResponse> items;
}
