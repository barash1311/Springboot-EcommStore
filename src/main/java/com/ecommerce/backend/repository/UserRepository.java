package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Enums.AppRole;
import com.ecommerce.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.roleName = :role")
    Page<User> findByRoleName(@Param("role") AppRole role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.roleName = 'ROLE_SELLER'")
    Page<User> findAllSellers(Pageable pageable);
}
