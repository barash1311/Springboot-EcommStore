package com.ecommerce.backend.service.authentication;
import com.ecommerce.backend.dto.authentication.*;
import com.ecommerce.backend.entity.Enums.AppRole;
import com.ecommerce.backend.entity.role.Role;
import com.ecommerce.backend.entity.role.UserRole;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.repository.RoleRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.security.jwt.JwtUtils;
import com.ecommerce.backend.security.services.UserDetailsImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // ---------------- SIGN IN ----------------
    @Override
    public SignInResponse signIn(SignInRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getUserRoles()
                .stream()
                .map(ur -> ur.getRole().getRoleName().name())
                .collect(Collectors.toList());

        return new SignInResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                roles,
                jwt
        );
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        userRepository.save(user);

        Set<String> strRoles = request.getRoles();
        Set<UserRole> userRoleSet = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            userRoleSet.add(new UserRole(user, userRole));
        } else {
            strRoles.forEach(roleName -> {
                AppRole appRole = AppRole.valueOf(roleName.toUpperCase());
                Role role = roleRepository.findByRoleName(appRole)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                userRoleSet.add(new UserRole(user, role));
            });
        }

        user.setUserRoles(new ArrayList<>(userRoleSet));
        userRepository.save(user);

        return new SignUpResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                "User registered successfully"
        );
    }

    @Override
    public ResponseCookie signOut() {
        return jwtUtils.clearJwtCookie();
    }

    @Override
    public UserInfoDTO getCurrentUser(Authentication authentication) {

        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getUserRoles()
                .stream()
                .map(ur -> ur.getRole().getRoleName().name())
                .collect(Collectors.toList());

        return new UserInfoDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                roles
        );
    }

    @Override
    public Object getAllSellers(Pageable pageable) {
        return userRepository.findAllSellers(pageable);
    }
}
