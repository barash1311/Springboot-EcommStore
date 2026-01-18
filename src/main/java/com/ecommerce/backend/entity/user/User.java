package com.ecommerce.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",uniqueConstraints ={
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
} )
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @NotBlank
    @Size(max = 50)
    private String username;
    @NotBlank
    @Email
    @Size(max = 120)
    private String email;
    @NotBlank
    @Size(max = 120)
    private String password;
}
