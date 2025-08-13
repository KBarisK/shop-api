package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.OrderMapper;
import com.staj.gib.shopapi.dto.request.CashOrderRequest;
import com.staj.gib.shopapi.dto.request.InstallmentOrderRequest;
import com.staj.gib.shopapi.dto.response.CartOrderDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.enums.PaymentMethod;
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
    public OrderResponse placeCashOrder(CashOrderRequest orderRequest) {
        return placeOrderInternal(orderRequest.getCartId(), PaymentMethod.PAYMENT_CASH,
                0);
    }

    @Transactional
    public OrderResponse placeInstallmentOrder(InstallmentOrderRequest orderRequest) {
        return placeOrderInternal(orderRequest.getCartId(), PaymentMethod.PAYMENT_INSTALLMENT,
                orderRequest.getInstallmentCount().getValue());
    }


    private OrderResponse placeOrderInternal(UUID cartId, PaymentMethod paymentMethod, int installmentCount) {
        CartOrderDto cart = orderValidator.validateAndGetCart(cartId);

        BigDecimal totalAmount = orderValidator.calculateOrderTotal(cart.getCartItems(), paymentMethod, installmentCount);

        Order order = orderValidator.createInitialOrder(cart.getUserId(),totalAmount, paymentMethod);

        List<OrderItem> orderItems = orderValidator.processCartItems(cart.getCartItems(), order);
        order.setOrderItems(orderItems);

        orderValidator.handlePaymentMethod(order, paymentMethod, installmentCount);
        
        Order savedOrder = orderRepository.save(order);

        cartService.removeAllItemsFromCart(cartId);
        
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
