package com.ecommerce.backend.entity.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank
    private String productName;
    private String image;
    @NotBlank
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;


}
