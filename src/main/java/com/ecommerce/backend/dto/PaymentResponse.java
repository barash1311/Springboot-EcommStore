package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String clientSecret;    // Stripe client secret for frontend
    private String paymentStatus;   // e.g. "pending", "succeeded", "failed"
    private String paymentMethod;   // e.g. "stripe"
    private String message;
}
