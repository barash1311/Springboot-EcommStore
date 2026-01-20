package com.ecommerce.backend.service.product;


import com.ecommerce.backend.dto.product.ProductRequest;
import com.ecommerce.backend.dto.product.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category);

    ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse addProduct(Long categoryId, @Valid ProductRequest productRequest);

    ProductRequest updateProduct(Long productId, @Valid ProductRequest productRequest);

    ProductRequest deleteProduct(Long productId);

    ProductRequest updateProductImage(Long productId, MultipartFile image);

    ProductResponse getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductRequest updateProductSeller(Long productId, @Valid ProductRequest productRequest);

    ProductRequest deleteProductSeller(Long productId);

    ProductRequest updateProductImageSeller(Long productId, MultipartFile image);
}
