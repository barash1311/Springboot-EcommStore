package com.ecommerce.backend.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class SignInResponse {

    private Long userId;
    private String username;
    private String email;
    private List<String> roles;

    // Returned only if you want JWT in body
    private String jwtToken;
}
