package com.ecommerce.backend.controller;

import com.ecommerce.backend.configs.AppConstants;
import com.ecommerce.backend.dto.product.ProductRequest;
import com.ecommerce.backend.dto.product.ProductResponse;
import com.ecommerce.backend.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    public final ProductService productService;

    //Public APIs
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        return ResponseEntity.ok(productService.getAllProducts(pageNumber,
                pageSize,
                sortBy,
                sortOrder,
                keyword,
                category));
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        return ResponseEntity.ok(productService.getProductByCategory(categoryId,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder));
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        return ResponseEntity.ok(productService.getProductByKeyword(
                keyword,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder
        ));
    }
    //ADMIN API
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> addProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody ProductRequest productRequest
            ){
        return ResponseEntity.ok(productService.addProduct(categoryId,productRequest));
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequest> updateProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(productId, productRequest)
        );
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequest> deleteProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                productService.deleteProduct(productId)
        );
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductRequest> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image
    ) {
        return ResponseEntity.ok(
                productService.updateProductImage(productId, image)
        );
    }

    //seller apis
    @GetMapping("/seller/products")
    public ResponseEntity<ProductResponse> getAllProductsForSeller(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        return ResponseEntity.ok(
                productService.getAllProductsForSeller(
                        pageNumber, pageSize, sortBy, sortOrder
                )
        );
    }

    @PutMapping("/seller/products/{productId}")
    public ResponseEntity<ProductRequest> updateProductSeller(
            @Valid @RequestBody ProductRequest productRequest,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                productService.updateProductSeller(productId, productRequest)
        );
    }

    @DeleteMapping("/seller/products/{productId}")
    public ResponseEntity<ProductRequest> deleteProductSeller(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                productService.deleteProductSeller(productId)
        );
    }

    @PutMapping("/seller/products/{productId}/image")
    public ResponseEntity<ProductRequest> updateProductImageSeller(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image
    ) {
        return ResponseEntity.ok(
                productService.updateProductImageSeller(productId, image)
        );
    }


}
