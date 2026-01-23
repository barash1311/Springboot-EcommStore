package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);

    void deleteCartItemByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);
    void deleteAllByCart_CartId(Long cartId);
}
