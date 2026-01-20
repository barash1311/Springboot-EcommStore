package com.ecommerce.backend.utils;

import com.ecommerce.backend.Exceptions.APIException;
import com.ecommerce.backend.Exceptions.ResourceNotFoundException;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new APIException("User is not authenticated");
        }

        String username = authentication.getName();

        return userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "username", username));
    }

    public User loggedInUser() {
        return getAuthenticatedUser();
    }

    public Long loggedInUserId() {
        return getAuthenticatedUser().getUserId();
    }

    public String loggedInEmail() {
        return getAuthenticatedUser().getEmail();
    }
}

