package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);

    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);

    void deleteAllByCartId(Long cartId);
}
