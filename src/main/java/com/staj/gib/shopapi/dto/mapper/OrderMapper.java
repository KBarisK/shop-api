package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    OrderItem cartItemDtoToOrderItem(CartItemDto cartItemDto);

    List<OrderResponse> toOrderResponseList(List<Order> orders);
}
