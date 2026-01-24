package com.ecommerce.backend.dto;

import lombok.Data;

@Data
public class AnalyticsResponse {
    private long totalUsers;
    private long totalOrders;
    private double totalRevenue;
}
