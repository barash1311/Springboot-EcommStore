package com.ecommerce.backend.entity.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "pg_payment_id")
    private String pgPaymentId;

    @Column(name = "pg_status")
    private String pgStatus;

    @Column(name = "pg_response_message")
    private String pgResponseMessage;

    @Column(name = "pg_name")
    private String pgName;
}
