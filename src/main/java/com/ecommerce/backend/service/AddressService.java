package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AddressRequest;
import com.ecommerce.backend.dto.AddressResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(@Valid AddressRequest request);

    List<AddressResponse> getAllAddresses();

    AddressResponse getAddressById(Long addressId);

    List<AddressResponse> getMyAddresses();

    AddressResponse updateAddress(Long addressId, @Valid AddressRequest request);

    String deleteAddress(Long addressId);
}
