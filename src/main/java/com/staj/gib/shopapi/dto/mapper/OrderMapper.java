package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.request.InitialOrderRequest;
import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.dto.response.OrderItemDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import com.staj.gib.shopapi.enums.OrderStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "product",  ignore = true)
    OrderItem cartItemDtoToOrderItem(CartItemDto cartItemDto);

    List<OrderResponse> toOrderResponseList(List<Order> orders);


    @Mapping(target = "status", expression = "java(determineInitialOrderStatus(request.getPaymentMethod()))")
    @Mapping(target = "totalAmount", source = "request.totalAmount")
    @Mapping(target = "paymentMethod", source = "request.paymentMethod")
    @Mapping(target = "orderItems", expression = "java(new ArrayList<>())")
    @Mapping(target = "userId", source = "request.userId")
    Order toInitialOrder(InitialOrderRequest request);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    default OrderStatus determineInitialOrderStatus(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.PAYMENT_CASH
                ? OrderStatus.FINISHED
                : OrderStatus.ACTIVE;
    }
}
