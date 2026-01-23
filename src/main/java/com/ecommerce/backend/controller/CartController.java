package com.ecommerce.backend.controller;

import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final CartService cartService;

    @PostMapping("/cart/create")
    public ResponseEntity<>
}
