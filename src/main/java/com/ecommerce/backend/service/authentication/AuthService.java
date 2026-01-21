package com.ecommerce.backend.service.authentication;

import com.ecommerce.backend.dto.authentication.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    // Authenticate user and return user info + JWT token
    SignInResponse signIn(SignInRequest request);

    SignUpResponse signUp(SignUpRequest request);

    ResponseCookie signOut();

    // ---------- USER ----------
    UserInfoDTO getCurrentUser(Authentication authentication);

    // ---------- SELLERS ----------
    Object getAllSellers(Pageable pageable);
}
