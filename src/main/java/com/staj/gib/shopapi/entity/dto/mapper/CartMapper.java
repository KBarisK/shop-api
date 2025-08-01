package com.staj.gib.shopapi.entity.dto.mapper;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.dto.CartDto;
import com.staj.gib.shopapi.entity.dto.CartItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper{

    @Mapping(target = "productId", source = "product.id")
    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    List<CartItemDto> cartItemsToCartItemDtos(List<CartItem> cartItems);

    CartDto cartToCartDto(Cart cart);
}
