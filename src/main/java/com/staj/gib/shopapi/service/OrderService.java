package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.OrderMapper;
import com.staj.gib.shopapi.dto.request.OrderRequest;
import com.staj.gib.shopapi.dto.response.CartOrderDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.OrderRepository;
import com.staj.gib.shopapi.service.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final CartService cartService;
    private final OrderValidator orderValidator;

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        CartOrderDto cart = orderValidator.validateAndGetCart(orderRequest.getCartId());

        BigDecimal totalAmount = orderValidator.calculateOrderTotal(cart.getCartItems(), orderRequest);

        Order order = orderValidator.createInitialOrder(cart.getUserId(),totalAmount, orderRequest.getPaymentMethod());

        List<OrderItem> orderItems = orderValidator.processCartItems(cart.getCartItems(), order);
        order.setOrderItems(orderItems);

        orderValidator.handlePaymentMethod(order, orderRequest);
        
        Order savedOrder = orderRepository.save(order);

        cartService.removeAllItemsFromCart(orderRequest.getCartId());
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersOfUser(UUID userId) {
        return orderMapper.toOrderResponseList(this.orderRepository.findAllByUserId(userId));

    }
    public OrderResponse getOrder(UUID orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND, orderId));
        return this.orderMapper.toOrderResponse(order);
    }


}
