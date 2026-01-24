package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.*;

public interface OrderService {


    OrderDetailsResponse placeOrder(PlaceOrderRequest request);


    OrderPageResponse getAllOrders(PaginationRequest paginationRequest);


    OrderPageResponse getSellerOrders(PaginationRequest paginationRequest);


    OrderDetailsResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    );

    OrderDetailsResponse getOrderDetails(Long orderId);
}
