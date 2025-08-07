package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.ProductMapper;
import com.staj.gib.shopapi.dto.request.CartRequest;
import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.dto.mapper.CartMapper;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final ProductMapper productMapper;


    public CartDto getActiveCart(UUID userId) {
        Cart cart = this.cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart for User",userId));
        return this.cartMapper.cartToCartDto(cart);
    }

    public CartDto addItemToCart(CartRequest cartRequest) {
        Cart cart = cartRepository.findByUser_Id(cartRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("UserCart", cartRequest.getUserId()));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId()
                        .equals(cartRequest.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            short newQuantity = (short) (item.getQuantity() + cartRequest.getQuantity());
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(cartRequest.getProductId());
            newItem.setCart(cart);
            newItem.setQuantity(cartRequest.getQuantity());

            cart.getCartItems().add(newItem);
        }
        Cart savedCart = cartRepository.save(cart);
        return this.cartMapper.cartToCartDto(savedCart);
    }

    public CartDto removeItemFromCart(CartRequest cartRequest) {
        Cart cart = cartRepository.findByUser_Id(cartRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("UserCart", cartRequest.getUserId()));
        cart.getCartItems().removeIf(item -> { //true means delete false means keep the item.
            if (item.getProduct().getId().equals(cartRequest.getProductId())) {
                short currentQuantity = item.getQuantity();
                if (cartRequest.getQuantity() >= currentQuantity) {
                    return true;
                } else {
                    item.setQuantity((short) (currentQuantity - cartRequest.getQuantity()));
                    return false;
                }
            }
            return false;
        });
        Cart savedCart = cartRepository.save(cart);
        return this.cartMapper.cartToCartDto(savedCart);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        Cart cart  = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public CartDto getCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        return this.cartMapper.cartToCartDto(cart);
    }

    public void createCart(UUID userId) {
        Cart newCart = this.cartMapper.createCartFromRequest(userId);
        this.cartRepository.save(newCart);
    }


}
