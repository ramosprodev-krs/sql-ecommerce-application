package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.entity.CartEntity;
import com.ramosprodev.sql_application.entity.CartItemEntity;
import com.ramosprodev.sql_application.entity.ProductEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.repository.CartRepository;
import com.ramosprodev.sql_application.repository.ProductRepository;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * The CartService class is responsible for 4 methods (the auxiliary method was created for a cleaner code):
     * 1. Cart item addition
     * 2. Cart item removal
     * 3. Cart clearing
     * 4. Cart reading
     **/

    // 1. Add item to cart
    @Transactional
    public CartEntity addItemToCart(Long productId, Integer quantity) {
        // Selects the current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        var selectedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found."));

        // Selects the user's cart
        var selectedCart = selectedUser.getCart();

        // Selects an optional item
        var selectedItem = selectedCart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        // Validates if the item is already present in the cart or not
        if (selectedItem.isPresent()) {
            CartItemEntity newItem = selectedItem.get();
            newItem.setQuantity(newItem.getQuantity() + quantity);
            newItem.setTotalPrice(newItem.getTotalPrice().add(newItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity))));
            selectedCart.setCartPrice(selectedCart.getCartPrice().add(newItem.getTotalPrice()));
            selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() - quantity);
        } else {
            CartItemEntity cartItem = getCartItemEntity(quantity, selectedProduct, selectedCart);
            selectedCart.getCartItems().add(cartItem);
            selectedCart.setCartPrice(selectedCart.getCartPrice().add(cartItem.getTotalPrice()));
            selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() - quantity);
        }

        productRepository.save(selectedProduct);
        return cartRepository.save(selectedCart);
    }

    // 2. Remove cart item
    @Transactional
    public void removeCartItem(Long cartItemId) {
        // Selects the current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        CartEntity selectedCart = selectedUser.getCart();

        // Selects the cart item
        var selectedItem = selectedCart.getCartItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cart item not found."));


        // Selects the original product
        var selectedProduct = productRepository.findById(selectedItem.getProduct().getId())
                .orElseThrow(() -> new NoSuchElementException("Product not found."));

        selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() + selectedItem.getQuantity());
        selectedCart.getCartItems().remove(selectedItem);

        productRepository.save(selectedProduct);
        cartRepository.save(selectedCart);
    }

    // 3. Clear cart
    public CartEntity clearCart() {
        // Selects the current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        CartEntity selectedCart = selectedUser.getCart();

        // Retrieve stock quantity
        selectedCart.getCartItems().forEach(
                i -> {
                    ProductEntity originalProduct = i.getProduct();
                    originalProduct.setStockQuantity(originalProduct.getStockQuantity() + i.getQuantity());
                });

        selectedCart.getCartItems().clear();
        selectedCart.setCartPrice(BigDecimal.ZERO);
        return cartRepository.save(selectedCart);
    }

    // 4. Read cart
    @Transactional
    public CartEntity readCart() {
        // Selects the current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        return selectedUser.getCart();
    }


    // Auxiliary methods

    // 1. Extracted method
    private static CartItemEntity getCartItemEntity(Integer quantity, ProductEntity selectedProduct, CartEntity selectedCart) {
        // Ensures the product is available
        if (selectedProduct.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("There are only " + selectedProduct.getStockQuantity() + "units available");
        }

        var totalPrice = selectedProduct.getPrice().multiply(BigDecimal.valueOf(quantity));

        // Creates a new cart item
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setCart(selectedCart);
        cartItem.setProduct(selectedProduct);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(selectedProduct.getPrice());
        cartItem.setTotalPrice(totalPrice);
        return cartItem;
    }

}
