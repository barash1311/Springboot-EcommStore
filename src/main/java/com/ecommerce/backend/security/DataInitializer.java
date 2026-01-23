package com.ecommerce.backend.security;

import com.ecommerce.backend.entity.Enums.AppRole;
import com.ecommerce.backend.entity.role.Role;
import com.ecommerce.backend.entity.role.UserRole;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.repository.RoleRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_SELLER)));
        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

        createUserIfNotExists("user1", "user1@example.com", "password1", Set.of(userRole));
        createUserIfNotExists("seller1", "seller1@example.com", "password2", Set.of(sellerRole));
        createUserIfNotExists("admin", "admin@example.com", "adminPass", Set.of(userRole, sellerRole, adminRole));
    }

    private void createUserIfNotExists(String username, String email, String password, Set<Role> roles) {
        Optional<User> existingUser = userRepository.findByUserName(username);

        if (existingUser.isEmpty()) {
            User user = new User();
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            for (Role role : roles) {
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                user.getUserRoles().add(userRole);
            }

            userRepository.save(user);
        }
    }
}
