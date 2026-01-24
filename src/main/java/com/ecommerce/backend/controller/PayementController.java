
package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.StripePaymentRequest;
import com.ecommerce.backend.dto.PaymentResponse;
import com.ecommerce.backend.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PayementController {

    private final StripeService paymentService;

    /**
     * Create a payment intent (Stripe)
     * Called from frontend AFTER order is created
     */
    @PostMapping("/stripe/create-intent")
    public ResponseEntity<PaymentResponse> createStripePayment(
            @RequestBody StripePaymentRequest request
    ) {
        return ResponseEntity.ok(
                paymentService.createStripePaymentIntent(request)
        );
    }

    /**
     * Stripe webhook endpoint
     * Stripe calls THIS endpoint, not frontend
     */
    @PostMapping("/stripe/webhook")
    public ResponseEntity<Void> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature
    ) {
        paymentService.handleStripeWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }

    /**
     * Get payment details by orderId
     * Used by frontend to check payment status
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(orderId)
        );
    }
}
