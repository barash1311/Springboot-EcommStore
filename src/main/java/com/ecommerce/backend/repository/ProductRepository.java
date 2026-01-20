package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.category.Category;
import com.ecommerce.backend.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
