package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.CartItemRequest;
import com.ecommerce.backend.dto.CartItemResponse;
import com.ecommerce.backend.dto.CartRequest;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart/create")
    public ResponseEntity<CartResponse> createCart(
            @RequestBody CartRequest request
    ){
        return ResponseEntity.ok(cartService.createCart(request));
    }
    @PutMapping("/cart/update")
    public ResponseEntity<CartResponse> updateCart(
            @RequestBody CartRequest request
    ){
        return ResponseEntity.ok(cartService.updateCart(request));
    }
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addProductToCart(
            @RequestBody CartItemRequest request
    ) {
        return ResponseEntity.ok(cartService.addProductToCart(request));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateProductQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(
                cartService.updateProductQuantity(productId, quantity));
    }
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable Long productId
    ) {
        cartService.removeProductFromCart(productId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<CartResponse> getMyCart() {
        return ResponseEntity.ok(cartService.getMyCart());
    }
    @GetMapping("/all")
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

}
