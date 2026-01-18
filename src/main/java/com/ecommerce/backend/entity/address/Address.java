package com.ecommerce.backend.entity.address;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @NotBlank
    private String street;
    @NotBlank
    private String buildingName;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
    @NotBlank
    private String pincode;

}
