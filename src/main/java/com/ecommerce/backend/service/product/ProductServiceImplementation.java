package com.ecommerce.backend.service.product;

import com.ecommerce.backend.dto.product.ProductRequest;
import com.ecommerce.backend.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public ProductResponse addProduct(Long categoryId, ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductRequest updateProduct(Long productId, ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductRequest deleteProduct(Long productId) {
        return null;
    }

    @Override
    public ProductRequest updateProductImage(Long productId, MultipartFile image) {
        return null;
    }

    @Override
    public ProductResponse getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductRequest updateProductSeller(Long productId, ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductRequest deleteProductSeller(Long productId) {
        return null;
    }

    @Override
    public ProductRequest updateProductImageSeller(Long productId, MultipartFile image) {
        return null;
    }
}
