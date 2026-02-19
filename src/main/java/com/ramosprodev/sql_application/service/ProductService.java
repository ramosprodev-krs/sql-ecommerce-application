package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.dto.ProductDTO;
import com.ramosprodev.sql_application.entity.ProductEntity;
import com.ramosprodev.sql_application.repository.ProductRepository;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /*
     * The following methods compose the CRUD methods, in this case for the product management.
     * This class acts the same way as the UserService.
     */

    // CRUD Methods:

    // 1. Create product
    @Transactional
    public ProductEntity createProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("Product DTO can't be null.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        ProductEntity product = new ProductEntity();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setPrice(productDTO.getPrice());
        product.setCreator(currentUser);
        currentUser.getProductsList().add(product);

        return productRepository.save(product);
    }

    // 2. Read all products
    @Transactional
    public List<ProductEntity> readAllProducts() {
        return productRepository.findAll();
    }

    // 2.1 Read a a single product
    @Transactional
    public ProductEntity readProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found."));
    }

    // 3. Update a single product
    @Transactional
    public ProductEntity updateProduct(Long id, ProductDTO productDTO) {
        var selectedUser = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found."));

        if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
            selectedUser.setName(productDTO.getName());
        }

        if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            selectedUser.setDescription(productDTO.getDescription());
        }

        if (productDTO.getStockQuantity() != null) {
            selectedUser.setStockQuantity(productDTO.getStockQuantity());
        }

        if (productDTO.getPrice() != null) {
            selectedUser.setPrice(productDTO.getPrice());
        }

        return productRepository.save(selectedUser);
    }

    // 4. Delete a single product
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found.");
        }

        productRepository.deleteById(id);

    }


}
