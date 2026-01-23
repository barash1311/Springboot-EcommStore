package com.ecommerce.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripePaymentRequest {
    private Double amount;          // total amount to charge
    private String currency;        // e.g. "usd" or "inr"
    private String description;     // optional description
    private String customerEmail;
}
