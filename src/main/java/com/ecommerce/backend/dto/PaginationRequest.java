package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortOrder;
}
