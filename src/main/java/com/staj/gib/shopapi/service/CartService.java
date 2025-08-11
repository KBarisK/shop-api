package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.ProductMapper;
import com.staj.gib.shopapi.dto.request.CartRequest;
import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.dto.response.CartOrderDto;
import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.dto.mapper.CartMapper;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final ProductMapper productMapper;


    public CartDto getActiveCart(UUID userId) {
        Cart cart = this.cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_FOR_USER_NOT_FOUND, userId));

        return this.cartMapper.cartToCartDto(cart);
    }

    @Transactional
    public CartDto addItemToCart(CartRequest cartRequest) {
        Cart cart = cartRepository.findByUser_Id(cartRequest.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_FOR_USER_NOT_FOUND, cartRequest.getUserId()));

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

    @Transactional
    public CartDto removeItemFromCart(CartRequest cartRequest) {
        Cart cart = cartRepository.findByUser_Id(cartRequest.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_FOR_USER_NOT_FOUND, cartRequest.getUserId()));
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

    @Transactional
    public void removeAllItemsFromCart(UUID cartId) {
        Cart cart  = cartRepository.findById(cartId).orElseThrow(() ->new BusinessException(ErrorCode.CART_NOT_FOUND,cartId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public CartOrderDto getCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND,cartId));
        return this.cartMapper.cartToCartOrderDto(cart);
    }

    @Transactional
    public void createCart(UUID userId) {
        Cart newCart = this.cartMapper.createCartFromRequest(userId);
        this.cartRepository.save(newCart);
    }


}
