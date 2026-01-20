package com.ecommerce.backend.service.product;

import com.ecommerce.backend.Exceptions.APIException;
import com.ecommerce.backend.Exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dto.product.ProductRequest;
import com.ecommerce.backend.dto.product.ProductResponse;
import com.ecommerce.backend.entity.category.Category;
import com.ecommerce.backend.entity.product.Product;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.service.File.FileService;
import com.ecommerce.backend.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final AuthUtil authUtil;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Value("${project.image}")
    private String imagePath;

    /* ============================================================
                            PUBLIC APIs
       ============================================================ */

    @Override
    public ProductResponse getAllProducts(Integer pageNumber,
                                          Integer pageSize,
                                          String sortBy,
                                          String sortOrder,
                                          String keyword,
                                          String category) {

        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, sortOrder);

        Page<Product> page;

        if (isNotBlank(keyword) && isNotBlank(category)) {
            page = productRepository
                    .findByProductNameContainingIgnoreCaseAndCategoryCategoryNameIgnoreCase(
                            keyword.trim(), category.trim(), pageable);
        } else if (isNotBlank(keyword)) {
            page = productRepository
                    .findByProductNameContainingIgnoreCase(keyword.trim(), pageable);
        } else if (isNotBlank(category)) {
            page = productRepository
                    .findByCategoryCategoryNameIgnoreCase(category.trim(), pageable);
        } else {
            page = productRepository.findAll(pageable);
        }

        if (page.isEmpty()) {
            throw new APIException("No products found");
        }

        return buildProductResponse(page);
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId,
                                                Integer pageNumber,
                                                Integer pageSize,
                                                String sortBy,
                                                String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", categoryId));

        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> page = productRepository.findByCategory(category, pageable);

        if (page.isEmpty()) {
            throw new APIException("No products found in category: " + category.getCategoryName());
        }

        return buildProductResponse(page);
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword,
                                               Integer pageNumber,
                                               Integer pageSize,
                                               String sortBy,
                                               String sortOrder) {

        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, sortOrder);

        Page<Product> page = productRepository
                .findByProductNameContainingIgnoreCase(keyword.trim(), pageable);

        if (page.isEmpty()) {
            throw new APIException("No products found for keyword: " + keyword);
        }

        return buildProductResponse(page);
    }

    /* ============================================================
                            ADMIN APIs
       ============================================================ */

    @Override
    public ProductResponse addProduct(Long categoryId, ProductRequest productRequest) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", categoryId));

        if (productRepository.existsByProductNameIgnoreCaseAndCategory(
                productRequest.getProductName(), category)) {
            throw new APIException("Product already exists in this category");
        }

        Product product = modelMapper.map(productRequest, Product.class);
        product.setCategory(category);
        product.setSeller(authUtil.loggedInUser());
        product.setImage("default.png");

        calculateSpecialPrice(product);

        Product saved = productRepository.save(product);

        return singleProductResponse(saved);
    }

    @Override
    public ProductRequest updateProduct(Long productId, ProductRequest productRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", productId));

        modelMapper.map(productRequest, product);
        calculateSpecialPrice(product);

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductRequest deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", productId));

        productRepository.delete(product);
        return mapToResponse(product);
    }

    @Override
    public ProductRequest updateProductImage(Long productId, MultipartFile image) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", productId));

        try {
            String fileName = fileService.uploadImage(imagePath, image);
            product.setImage(fileName);
        } catch (Exception e) {
            throw new APIException("Image upload failed");
        }

        return mapToResponse(productRepository.save(product));
    }

    /* ============================================================
                            SELLER APIs
       ============================================================ */

    @Override
    public ProductResponse getAllProductsForSeller(Integer pageNumber,
                                                   Integer pageSize,
                                                   String sortBy,
                                                   String sortOrder) {

        User seller = authUtil.loggedInUser();
        Pageable pageable = createPageable(pageNumber, pageSize, sortBy, sortOrder);

        Page<Product> page = productRepository.findBySeller(seller, pageable);

        if (page.isEmpty()) {
            throw new APIException("You have not added any products yet");
        }

        return buildProductResponse(page);
    }

    @Override
    public ProductRequest updateProductSeller(Long productId, ProductRequest productRequest) {

        Product product = getProductOwnedBySeller(productId);
        modelMapper.map(productRequest, product);
        calculateSpecialPrice(product);

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductRequest deleteProductSeller(Long productId) {

        Product product = getProductOwnedBySeller(productId);
        productRepository.delete(product);
        return mapToResponse(product);
    }

    @Override
    public ProductRequest updateProductImageSeller(Long productId, MultipartFile image) {

        Product product = getProductOwnedBySeller(productId);

        try {
            String fileName = fileService.uploadImage(imagePath, image);
            product.setImage(fileName);
        } catch (Exception e) {
            throw new APIException("Image upload failed");
        }

        return mapToResponse(productRepository.save(product));
    }

    /* ============================================================
                        HELPER METHODS
       ============================================================ */

    private Pageable createPageable(Integer pageNumber, Integer pageSize,
                                    String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private ProductResponse buildProductResponse(Page<Product> page) {

        List<ProductRequest> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new ProductResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private ProductResponse singleProductResponse(Product product) {
        return new ProductResponse(
                List.of(mapToResponse(product)),
                0, 1, 1L, 1, true
        );
    }

    private ProductRequest mapToResponse(Product product) {
        ProductRequest dto = modelMapper.map(product, ProductRequest.class);
        dto.setImage(buildImageUrl(product.getImage()));
        return dto;
    }

    private String buildImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/")
                ? imageBaseUrl + imageName
                : imageBaseUrl + "/" + imageName;
    }

    private void calculateSpecialPrice(Product product) {
        if (product.getPrice() == null || product.getDiscount() == null) {
            product.setSpecialPrice(product.getPrice());
            return;
        }
        double discount = product.getPrice() * (product.getDiscount() / 100.0);
        product.setSpecialPrice(Math.round((product.getPrice() - discount) * 100.0) / 100.0);
    }

    private Product getProductOwnedBySeller(Long productId) {
        return productRepository.findByProductIdAndSeller(productId, authUtil.loggedInUser())
                .orElseThrow(() ->
                        new APIException("Product not found or not owned by you"));
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
