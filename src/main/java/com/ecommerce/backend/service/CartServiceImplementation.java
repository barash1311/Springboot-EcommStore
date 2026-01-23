package com.ecommerce.backend.service;


import com.ecommerce.backend.Exceptions.APIException;
import com.ecommerce.backend.Exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dto.CartItemRequest;
import com.ecommerce.backend.dto.CartItemResponse;
import com.ecommerce.backend.dto.CartRequest;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.entity.cart.Cart;
import com.ecommerce.backend.entity.cart.CartItem;
import com.ecommerce.backend.entity.product.Product;
import com.ecommerce.backend.repository.CartItemRepository;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements  CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public List<CartResponse> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        return carts.stream()
                .map(this::mapToCartResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CartResponse getMyCart() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        return mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public void removeProductFromCart(Long productId) {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(), productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(
                cart.getCartId(), productId);

        cartRepository.save(cart);
    }


    @Transactional
    @Override
    public CartResponse updateProductQuantity(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(), productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() +
                    " not available in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        // Calculate new quantity
        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative");
        }

        if (product.getQuantity() < newQuantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity());
        }

        if (newQuantity == 0) {
            removeProductFromCart(productId);
            return getMyCart();
        }

        // Update cart item
        double priceDifference = product.getSpecialPrice() * quantity;
        cart.setTotalPrice(cart.getTotalPrice() + priceDifference);

        cartItem.setQuantity(newQuantity);
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse addProductToCart(CartItemRequest request) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product",
                        "productId", request.getProductId()));

        CartItem existingCartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(), request.getProductId());

        if (existingCartItem != null) {
            throw new APIException("Product " + product.getProductName() +
                    " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < request.getQuantity()) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(request.getQuantity());
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() +
                (product.getSpecialPrice() * request.getQuantity()));
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse createCart(CartRequest request) {
        String emailId = authUtil.loggedInEmail();
        Cart existingCart = cartRepository.findCartByEmail(emailId);

        if (existingCart != null) {
            throw new APIException("Cart already exists for user");
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        cart = cartRepository.save(cart);

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            cart = addItemsToCart(cart, request.getItems());
        }

        return mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse updateCart(CartRequest request) {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        // Clear existing items
        cartItemRepository.deleteAllByCartId(cart.getCartId());
        cart.setTotalPrice(0.00);

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            cart = addItemsToCart(cart, request.getItems());
        }

        return mapToCartResponse(cart);
    }

    private Cart addItemsToCart(Cart cart, List<CartItemRequest> cartItemRequests) {
        double totalPrice = 0.00;

        for (CartItemRequest itemRequest : cartItemRequests) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product",
                            "productId", itemRequest.getProductId()));

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new APIException("Insufficient quantity for product: " +
                        product.getProductName());
            }

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(itemRequest.getQuantity());
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);

            totalPrice += product.getSpecialPrice() * itemRequest.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }


    private Cart getOrCreateCart() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        if (cart != null) {
            return cart;
        }

        Cart newCart = new Cart();
        newCart.setTotalPrice(0.00);
        newCart.setUser(authUtil.loggedInUser());
        return cartRepository.save(newCart);
    }


    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);

        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(cartItem -> {
                    CartItemResponse itemResponse = new CartItemResponse();
                    itemResponse.setProductId(cartItem.getProduct().getProductId());
                    itemResponse.setProductName(cartItem.getProduct().getProductName());
                    itemResponse.setPrice(cartItem.getProductPrice());
                    itemResponse.setDiscount(cartItem.getDiscount());
                    itemResponse.setQuantity(cartItem.getQuantity());
                    itemResponse.setSubTotal(cartItem.getProductPrice() * cartItem.getQuantity());
                    return itemResponse;
                })
                .collect(Collectors.toList());

        cartResponse.setItems(itemResponses);
        return cartResponse;
    }
}
