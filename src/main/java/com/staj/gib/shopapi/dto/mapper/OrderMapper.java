package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    OrderItem cartItemDtoToOrderItem(CartItemDto cartItemDto);
}
