package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AddressRequest;
import com.ecommerce.backend.dto.AddressResponse;
import com.ecommerce.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody AddressRequest request) {
        return  ResponseEntity.ok(addressService.createAddress(request));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(
            @PathVariable Long addressId) {
        AddressResponse response = addressService.getAddressById(addressId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressResponse>> getMyAddresses() {
        List<AddressResponse> addresses = addressService.getMyAddresses();
        return ResponseEntity.ok(addresses);
    }
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        AddressResponse response = addressService.updateAddress(addressId, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId) {
        String message = addressService.deleteAddress(addressId);
        return ResponseEntity.ok(message);
    }



}
