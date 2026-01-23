package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.PaymentResponse;
import com.ecommerce.backend.dto.StripePaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeServiceImplementation implements StripeService {
    @Override
    public PaymentResponse createStripePaymentIntent(StripePaymentRequest request) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (request.getAmount() * 100)); // Stripe expects cents
            params.put("currency", request.getCurrency());
            params.put("description", request.getDescription());

            PaymentIntent intent = PaymentIntent.create(params);

            return new PaymentResponse(
                    intent.getClientSecret(),
                    intent.getStatus(),
                    "stripe",
                    "Payment intent created successfully"
            );
        } catch (StripeException e) {
            return new PaymentResponse(null, "failed", "stripe", e.getMessage());
        }
    }

    @Override
    public void handleStripeWebhook(String payload, String signature) {
        // implement webhook verification and event handling
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        // fetch payment from DB if you store it
        return new PaymentResponse(null, "pending", "stripe", "Payment lookup not implemented");
    }

    @Override
    public PaymentResponse createPaymentIntent(StripePaymentRequest request) {
        try {
            // Set your Stripe secret key (move to config or environment variable later)
            com.stripe.Stripe.apiKey = "sk_test_your_secret_key";

            // Prepare parameters for PaymentIntent
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (request.getAmount() * 100)); // Stripe expects amount in cents
            params.put("currency", request.getCurrency());
            params.put("description", request.getDescription());

            // Create PaymentIntent
            PaymentIntent intent = PaymentIntent.create(params);

            // Return successful response
            return new PaymentResponse(
                    intent.getClientSecret(),
                    intent.getStatus(),
                    "stripe",
                    "Payment intent created successfully"
            );
        } catch (StripeException e) {
            // Return failed response
            return new PaymentResponse(
                    null,
                    "failed",
                    "stripe",
                    e.getMessage()
            );
        }
    }


}
