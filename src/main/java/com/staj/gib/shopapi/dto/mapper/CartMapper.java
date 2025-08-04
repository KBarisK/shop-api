package com.staj.gib.shopapi.entity.dto.mapper;

import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.enums.CartStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartMapper{

    @Mapping(target = "productId", source = "product.id")
    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    List<CartItemDto> cartItemsToCartItemDtos(List<CartItem> cartItems);

    CartDto cartToCartDto(Cart cart);

    @Mapping(target = "cartItems", expression = "java(new java.util.ArrayList<>())")
    Cart createCartFromRequest(UUID userId, CartStatus status);
}
