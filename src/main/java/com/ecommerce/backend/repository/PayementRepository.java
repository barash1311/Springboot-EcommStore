package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayementRepository extends JpaRepository<Payment,Long> {
    Payment findByOrder_OrderId(Long orderId);
}
