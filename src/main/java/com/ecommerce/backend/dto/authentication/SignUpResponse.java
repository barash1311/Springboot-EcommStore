package com.ecommerce.backend.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {

    private Long userId;
    private String username;
    private String email;
    private String message;
}
