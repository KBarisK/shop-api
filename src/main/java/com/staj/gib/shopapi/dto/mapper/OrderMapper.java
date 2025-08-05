package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem cartItemDtoToOrderItem(CartItemDto cartItemDto);
}
