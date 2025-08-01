package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.CartDto;
import com.staj.gib.shopapi.entity.dto.CartRepuest;
import com.staj.gib.shopapi.entity.dto.mapper.CartMapper;
import com.staj.gib.shopapi.enums.CartStatus;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.CartRepository;
import com.staj.gib.shopapi.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository; //TODO CHANGE IT TO SERVICE WHEN AVAILABLE
    private final CartRepository cartRepository;
    private final EntityManager entityManager;
    private final CartMapper cartMapper;


    public CartDto getActiveCart(UUID userId) {
        Optional<Cart> optionalCart = this.cartRepository.findByUser_IdAndStatus(userId, CartStatus.OPEN);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            return this.cartMapper.cartToCartDto(cart);
        }
        else{
            User user = this.entityManager.getReference(User.class,userId);
            Cart newCart = new Cart(user,CartStatus.OPEN,new ArrayList<>());
            Cart savedCart = this.cartRepository.save(newCart);
            return this.cartMapper.cartToCartDto(savedCart);
        }

    }

    public void addItemToCart(CartRepuest cartRepuest) {
        Product product = productRepository.findById(cartRepuest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product",cartRepuest.getProductId()));
        Cart cart = cartRepository.findById(cartRepuest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartRepuest.getCartId()));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartRepuest.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            short newQuantity = (short) (item.getQuantity() + cartRepuest.getQuantity());
            item.setQuantity(newQuantity);
        } else {
            //TODO YOU NEED TO CALCULATE TAX BEFORE ADDING THIS
            CartItem newItem = new CartItem(cart, product, product.getPrice(),product.getPrice(), cartRepuest.getQuantity());
            cart.getCartItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    public void removeItemFromCart(CartRepuest cartRepuest) {
        Cart cart = this.cartRepository.findById(cartRepuest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartRepuest.getCartId()));
        cart.getCartItems().removeIf(item -> { //true means delete false means keep the item.
            if (item.getProduct().getId().equals(cartRepuest.getProductId())) {
                short currentQuantity = item.getQuantity();
                if (cartRepuest.getQuantity() >= currentQuantity) {
                    return true;
                } else {
                    item.setQuantity((short) (currentQuantity - cartRepuest.getQuantity()));
                    return false;
                }
            }
            return false;
        });
        cartRepository.save(cart);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        Cart cart  = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        cart.setCartItems(new ArrayList<>());
        cartRepository.save(cart);
    }


}
