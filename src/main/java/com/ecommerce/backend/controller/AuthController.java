package com.ecommerce.backend.controller;

import com.ecommerce.backend.configs.AppConstants;
import com.ecommerce.backend.dto.authentication.*;
import com.ecommerce.backend.service.authentication.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ---------- SIGN IN ----------
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> authenticateUser(
            @Valid @RequestBody SignInRequest request) {

        SignInResponse response = authService.signIn(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getJwtToken())
                .body(response);
    }

    // ---------- SIGN UP ----------
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> registerUser(
            @Valid @RequestBody SignUpRequest request) {

        return ResponseEntity.ok(authService.signUp(request));
    }

    // ---------- CURRENT USERNAME (UNCHANGED UTILITY) ----------
    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        return authentication != null ? authentication.getName() : "";
    }

    // ---------- CURRENT USER DETAILS ----------
    @GetMapping("/user")
    public ResponseEntity<UserInfoDTO> getUserDetails(Authentication authentication) {
        return ResponseEntity.ok(authService.getCurrentUser(authentication));
    }

    // ---------- SIGN OUT ----------
    @PostMapping("/signout")
    public ResponseEntity<SignOutResponse> signoutUser() {

        ResponseCookie cookie = authService.signOut();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SignOutResponse("You've been signed out!"));
    }

    // ---------- SELLERS ----------
    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            ) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(
                pageNumber,
                Integer.parseInt(AppConstants.PAGE_SIZE),
                sortByAndOrder
        );

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));
    }
}