package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.*;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.StripeService;
import com.ecommerce.backend.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final StripeService stripeService;


    @PostMapping
    public ResponseEntity<OrderDetailsResponse> placeOrder(
            @RequestBody PlaceOrderRequest request
    ) {
        return  ResponseEntity.ok(orderService.placeOrder(request));
    }

    @PostMapping("/stripe/client-secret")
    public ResponseEntity<PaymentResponse> createStripeClientSecret(
            @RequestBody StripePaymentRequest request
    ){
        return  ResponseEntity.ok(stripeService.createPaymentIntent(request));
    }

    @GetMapping("/admin")
    public ResponseEntity<OrderPageResponse> getAllOrders(
            PaginationRequest paginationRequest
    ) {
        return ResponseEntity.ok(
                orderService.getAllOrders(paginationRequest)
        );
    }

    @GetMapping("/seller")
    public ResponseEntity<OrderPageResponse> getSellerOrders(
            PaginationRequest paginationRequest
    ) {
        return ResponseEntity.ok(
                orderService.getSellerOrders(paginationRequest)
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDetailsResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, request)
        );
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                orderService.getOrderDetails(orderId)
        );
    }



}
