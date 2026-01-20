package com.ecommerce.backend.entity.product;

import com.ecommerce.backend.entity.category.Category;
import com.ecommerce.backend.entity.user.User;
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
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column
    private String image;
    @Column(nullable = false)
    private String description;
    @Column
    private Integer quantity;
    @Column
    private Double price;
    @Column
    private Double discount;
    @Column(name = "special_price")
    private Double specialPrice;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;


}
