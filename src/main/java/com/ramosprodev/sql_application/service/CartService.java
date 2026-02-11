package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.entity.CartEntity;
import com.ramosprodev.sql_application.entity.CartItemEntity;
import com.ramosprodev.sql_application.entity.ProductEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.repository.CartRepository;
import com.ramosprodev.sql_application.repository.ProductRepository;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;


    public CartService(UserRepository userRepository, ProductRepository productRepository,
                       CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    /*
     * This class is focused on providing methods for actions such as:
     * Adding items to your cart
     * Emptying your cart
     */


    // 1. Add item to cart
    @Transactional
    public CartEntity addItemToCart(Long userId, Long productId, Integer quantity) {
        var selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        var selectedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found."));

        var selectedCart = selectedUser.getCart();

        selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() - quantity);

        CartItemEntity cartItem = getCartItemEntity(quantity, selectedProduct, selectedCart);

        selectedCart.getCartItems().add(cartItem);

        productRepository.save(selectedProduct);
        return cartRepository.save(selectedCart);
    }

    // 2. Remove cart item
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        UserEntity selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        CartEntity selectedCart = selectedUser.getCart();

        var selectedItem = selectedCart.getCartItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cart item not found."));

        var selectedProduct = productRepository.findById(selectedItem.getProduct().getId())
                .orElseThrow(() -> new NoSuchElementException("Product not found."));

        selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() + selectedItem.getQuantity());
        selectedCart.getCartItems().remove(selectedItem);

        productRepository.save(selectedProduct);
        cartRepository.save(selectedCart);
    }


    // Auxiliary methods

    // 1. Extracted method
    private static CartItemEntity getCartItemEntity(Integer quantity, ProductEntity selectedProduct, CartEntity selectedCart) {
        if (selectedProduct.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("There are only " + selectedProduct.getStockQuantity() + "units available");
        }

        var totalPrice = selectedProduct.getPrice().multiply(BigDecimal.valueOf(quantity));

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setCart(selectedCart);
        cartItem.setProduct(selectedProduct);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(selectedProduct.getPrice());
        cartItem.setTotalPrice(totalPrice);
        return cartItem;
    }

}
