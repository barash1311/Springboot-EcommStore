package com.ecommerce.backend.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    // Example: ["USER"], ["SELLER"]
    private Set<String> roles;
}