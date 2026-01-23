package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceOrderRequest {
    private Long addressId;
    private String paymentMethod;
    private String paymentGatewayName;
    private String paymentGatewayPaymentId;
    private String paymentStatus;
    private String paymentMessage;
}
