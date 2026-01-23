package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CartItemRequest;
import com.ecommerce.backend.dto.CartRequest;
import com.ecommerce.backend.dto.CartResponse;

import java.util.List;

public interface CartService {
    List<CartResponse> getAllCarts();
    CartResponse getMyCart();
    void removeProductFromCart(Long productId);
    CartResponse updateProductQuantity(Long productId, Integer quantity);
    CartResponse addProductToCart(CartItemRequest request);
    CartResponse createCart(CartRequest request);
    CartResponse updateCart(CartRequest request);
}
