package com.ecommerce.backend.service.product;

import com.ecommerce.backend.dto.product.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImplementation implements  ProductService {

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {
        return null;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }
}
