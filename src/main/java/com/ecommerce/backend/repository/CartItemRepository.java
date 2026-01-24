package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = :productId AND ci.cart.cartId = :cartId")
    CartItem findCartItemByProductIdAndCartId(@Param("cartId") Long cartId, @Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.product.productId = :productId AND ci.cart.cartId = :cartId")
    void deleteCartItemByProductIdAndCartId(@Param("cartId") Long cartId, @Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);

    CartItem findCartItemByProduct_ProductIdAndCart_CartId(Long cartId, Long productId);
}
