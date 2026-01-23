package com.ecommerce.backend.service;

import com.ecommerce.backend.Exceptions.APIException;
import com.ecommerce.backend.Exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dto.*;
import com.ecommerce.backend.entity.address.Address;
import com.ecommerce.backend.entity.cart.Cart;
import com.ecommerce.backend.entity.order.Order;

import com.ecommerce.backend.entity.order.OrderItem;
import com.ecommerce.backend.entity.payment.Payment;
import com.ecommerce.backend.entity.product.Product;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements  OrderService {
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final AuthUtil authUtil;

    @Override
    public OrderDetailsResponse placeOrder(PlaceOrderRequest request) {

        String email = authUtil.loggedInEmail();

        Cart cart = cartRepository.findCartByEmail(email);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", email);
        }

        if (cart.getCartItems().isEmpty()) {
            throw new APIException("Cart is empty");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.getAddressId()));
        // Create Order
        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("PLACED");
        order.setAddress(address);

        // Create Payment
        Payment payment = new Payment();
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPgPaymentId(request.getPaymentGatewayPaymentId());
        payment.setPgStatus(request.getPaymentStatus());
        payment.setPgResponseMessage(request.getPaymentMessage());
        payment.setPgName(request.getPaymentGatewayName());

        Order savedOrder = orderRepository.save(order);

        // Create Order Items
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new APIException("Insufficient stock for product: " + product.getProductName());
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setDiscount(cartItem.getDiscount());
            item.setOrderedProductPrice(cartItem.getProductPrice());

            return item;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        // Clear Cart
        cartService.clearCart();

        return mapToOrderDetailsResponse(savedOrder, orderItems);
    }




    @Override
    public OrderPageResponse getAllOrders(PaginationRequest paginationRequest) {

        Pageable pageable = buildPageable(paginationRequest);
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        return mapToOrderPageResponse(ordersPage);
    }



    @Override
    public OrderPageResponse getSellerOrders(PaginationRequest paginationRequest) {

        User seller = authUtil.loggedInUser();
        Pageable pageable = buildPageable(paginationRequest);

        Page<Order> ordersPage = orderRepository.findAll(pageable);

        List<Order> sellerOrders = ordersPage.getContent().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(item -> item.getProduct() != null
                                && item.getProduct().getSeller() != null
                                && item.getProduct().getSeller().getUserId().equals(seller.getUserId())))
                .toList();

        return new OrderPageResponse(
                sellerOrders.stream()
                        .map(order -> mapToOrderDetailsResponse(order, order.getOrderItems()))
                        .toList(),
                ordersPage.getNumber(),
                ordersPage.getSize(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages(),
                ordersPage.isLast()
        );
    }



    @Override
    public OrderDetailsResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.getStatus()));

        order.setOrderStatus(request.getStatus());
        orderRepository.save(order);

        return mapToOrderDetailsResponse(order, order.getOrderItems());
    }



    @Override
    public OrderDetailsResponse getOrderDetails(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        return mapToOrderDetailsResponse(order, order.getOrderItems());
    }



    private OrderDetailsResponse mapToOrderDetailsResponse(
            Order order,
            List<OrderItem> items
    ) {
        return new OrderDetailsResponse(
                order.getOrderId(),
                order.getEmail(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getAddress().getAddressId(),
                items.stream().map(this::mapToOrderItemResponse).toList()
        );
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        double total = (item.getOrderedProductPrice() - item.getDiscount())
                * item.getQuantity();

        return new OrderItemResponse(
                item.getOrderItemId(),
                item.getProduct().getProductId(),
                item.getProduct().getProductName(),
                item.getQuantity(),
                item.getOrderedProductPrice(),
                item.getDiscount(),
                total
        );
    }

    private OrderPageResponse mapToOrderPageResponse(Page<Order> page) {
        return new OrderPageResponse(
                page.getContent().stream()
                        .map(order -> mapToOrderDetailsResponse(order, order.getOrderItems()))
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private Pageable buildPageable(PaginationRequest request) {
        Sort sort = request.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        return PageRequest.of(
                request.getPageNumber(),
                request.getPageSize(),
                sort
        );
    }

}

