package com.ecommerce.backend.service;


import com.ecommerce.backend.dto.AnalyticsResponse;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    public AnalyticsResponse getAnalyticsData() {
        // Example mock data
        AnalyticsResponse response = new AnalyticsResponse();
        response.setTotalUsers(1200);
        response.setTotalOrders(340);
        response.setTotalRevenue(152000.75);
        return response;
    }
}