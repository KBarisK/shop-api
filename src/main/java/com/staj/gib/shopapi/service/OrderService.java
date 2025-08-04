package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.OrderMapper;
import com.staj.gib.shopapi.dto.request.OrderRequest;
import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.dto.response.CartItemDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final CartService cartService;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        this.cartService.removeAllItemsFromCart(orderRequest.getCartId());
        CartDto cart = this.cartService.getCart(orderRequest.getCartId());
        List<CartItemDto> cartItems = cart.getCartItems();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItemDto cartItem : cartItems) {

        }
        Order order = new Order();


        return this.orderMapper.toOrderResponse(order);
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", orderId));
        return this.orderMapper.toOrderResponse(order);
    }



}
