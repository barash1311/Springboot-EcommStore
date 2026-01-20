package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.category.Category;
import com.ecommerce.backend.entity.product.Product;
import com.ecommerce.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCaseAndCategoryCategoryNameIgnoreCase(
            String keyword, String categoryName, Pageable pageable);

    boolean existsByProductNameIgnoreCaseAndCategory(String productName, Category category);

    Page<Product> findBySeller(User seller, Pageable pageable);

    Page<Product> findByCategoryCategoryNameIgnoreCase(String categoryName, Pageable pageable);

    Optional<Product> findByProductIdAndSeller(Long productId, User seller);
}

