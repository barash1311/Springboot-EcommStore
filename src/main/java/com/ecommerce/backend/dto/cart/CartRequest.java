package com.ecommerce.backend.dto.cart;

import com.ecommerce.backend.dto.cartItem.CartItemRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CartRequest {
    private List<CartItemRequest> items;
}
