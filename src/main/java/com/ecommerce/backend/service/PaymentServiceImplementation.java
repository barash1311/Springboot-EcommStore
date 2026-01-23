package com.ecommerce.backend.service;



import com.ecommerce.backend.dto.PaymentResponse;
import com.ecommerce.backend.dto.StripePaymentRequest;
import com.ecommerce.backend.entity.payment.Payment;
import com.ecommerce.backend.repository.PayementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements PayementService {
    private final PayementRepository paymentRepository;

    @Override
    public PaymentResponse createStripePaymentIntent(StripePaymentRequest request) {
        // This is a fallback when Stripe is not used
        Payment payment = new Payment();
        payment.setPaymentMethod("manual");
        payment.setPgStatus("pending");
        payment.setPgResponseMessage("Manual payment created");
        payment.setPgName("offline");
        paymentRepository.save(payment);

        return new PaymentResponse(
                null,
                payment.getPgStatus(),
                payment.getPaymentMethod(),
                "Manual payment created successfully"
        );
    }

    @Override
    public void handleStripeWebhook(String payload, String signature) {
        // No webhook handling for manual payments
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId);
        if (payment == null) {
            return new PaymentResponse(null, "not_found", "manual", "No payment found for this order");
        }
        return new PaymentResponse(
                null,
                payment.getPgStatus(),
                payment.getPaymentMethod(),
                payment.getPgResponseMessage()
        );
    }
}
