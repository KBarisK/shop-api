package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.dto.response.CartOrderDto;
import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(config = CentralMapperConfig.class)
public interface CartMapper{

    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    List<CartItemDto> cartItemsToCartItemDtos(List<CartItem> cartItems);

    CartDto cartToCartDto(Cart cart);

    @Mapping(target = "cartItems", expression = "java(new java.util.ArrayList<>())")
    Cart createCartFromRequest(UUID userId);

    CartOrderDto cartToCartOrderDto(Cart cart);
}
