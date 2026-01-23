package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageResponse {
    private List<OrderDetailsResponse> orders;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private boolean last;
}
