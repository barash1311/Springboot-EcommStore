package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.PaymentResponse;
import com.ecommerce.backend.dto.StripePaymentRequest;

public interface StripeService {
    PaymentResponse createStripePaymentIntent(StripePaymentRequest request);
    void handleStripeWebhook(String payload, String signature);
    PaymentResponse getPaymentByOrderId(Long orderId);
    PaymentResponse createPaymentIntent(StripePaymentRequest request);
}
