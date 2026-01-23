package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
