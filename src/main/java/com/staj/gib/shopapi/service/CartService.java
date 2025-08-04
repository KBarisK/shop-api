package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.ProductMapper;
import com.staj.gib.shopapi.dto.request.CartRepuest;
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

    public CartDto addItemToCart(CartRepuest cartRepuest) {
        Cart cart = cartRepository.findById(cartRepuest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartRepuest.getCartId()));

        Optional<CartItem> existingItem = cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(cartRepuest.getProductId())).findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            short newQuantity = (short) (item.getQuantity() + cartRepuest.getQuantity());
            item.setQuantity(newQuantity);
        } else {
            Product product = this.productMapper.productFromId(cartRepuest.getProductId());
            CartItem newItem = new CartItem(cart, product, cartRepuest.getQuantity());
            cart.getCartItems().add(newItem);
        }
        Cart savedCart = cartRepository.save(cart);
        return this.cartMapper.cartToCartDto(savedCart);
    }

    public CartDto removeItemFromCart(CartRepuest cartRepuest) {
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
        Cart savedCart = cartRepository.save(cart);
        return this.cartMapper.cartToCartDto(savedCart);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        Cart cart  = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        cart.setCartItems(new ArrayList<>());
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
