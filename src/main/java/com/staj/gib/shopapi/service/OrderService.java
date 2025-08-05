package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.InstallmentPaymentMapper;
import com.staj.gib.shopapi.dto.mapper.OrderMapper;
import com.staj.gib.shopapi.dto.request.OrderRequest;
import com.staj.gib.shopapi.dto.response.*;
import com.staj.gib.shopapi.entity.CashPayment;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import com.staj.gib.shopapi.enums.OrderStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private final InstallmentPaymentMapper  installmentPaymentMapper;

    private final CartService cartService;
    private final TaxService taxService;
    private final CategoryService categoryService;
    private final InstallmentPaymentService installmentPaymentService;
    private final InstallmentService installmentService;
    private final ProductService productService;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        CartDto cart = this.cartService.getCart(orderRequest.getCartId());
        List<CartItemDto> cartItems = cart.getCartItems();
        BigDecimal totalAmount = calculateTotalAmount(cartItems);

        Order order = new Order();
        order.setStatus(OrderStatus.ACTIVE);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setOrderItems(new ArrayList<>());
        Order savedOrder = this.orderRepository.save(order);

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = createOrderItem(cartItem);
                    orderItem.setOrder(savedOrder);
                    this.productService.decrementStock(cartItem.getProduct().getId(), cartItem.getQuantity());
                    return orderItem;
                })
                .toList();

        savedOrder.setOrderItems(orderItems);
        if (orderRequest.getPaymentMethod().equals(PaymentMethod.PAYMENT_INSTALLMENT)){
            int installmentMonths = orderRequest.getInstallmentMonths();
            BigDecimal interestRate = orderRequest.getInterestRate();
            if (installmentMonths <= 0){
                throw new IllegalArgumentException("Installment Months has to be greater than 0");
            }

            InstallmentPaymentDto installmentPaymentDto = installmentPaymentService.saveInstallmentPayment(savedOrder,interestRate);
            savedOrder.setInstallmentPayment(this.installmentPaymentMapper.toEntity(installmentPaymentDto));

            this.installmentService.generateInstallments(savedOrder, installmentMonths, interestRate);

        }else if(orderRequest.getPaymentMethod().equals(PaymentMethod.PAYMENT_CASH)){
            CashPayment cashPayment = new CashPayment(savedOrder);
            savedOrder.setCashPayment(cashPayment);
            savedOrder.setStatus(OrderStatus.FINISHED);
        }

        orderRepository.save(savedOrder);

        this.cartService.removeAllItemsFromCart(orderRequest.getCartId());

        return this.orderMapper.toOrderResponse(order);
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", orderId));
        return this.orderMapper.toOrderResponse(order);
    }

    private BigDecimal calculateTotalAmount(List<CartItemDto> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItemDto cartItem : cartItems) {
            BigDecimal totalItemPrice = getProductTotalPrice(cartItem);

            BigDecimal itemTotalWithQuantity = totalItemPrice
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                    .setScale(SCALE, ROUNDING);

            totalPrice = totalPrice.add(itemTotalWithQuantity);
        }
        return totalPrice.setScale(SCALE, ROUNDING);
    }


    private OrderItem createOrderItem(CartItemDto cartItem) {
        OrderItem orderItem = orderMapper.cartItemDtoToOrderItem(cartItem);
        BigDecimal preTaxPrice = cartItem.getProduct().getPrice();
        BigDecimal price = getProductTotalPrice(cartItem);
        orderItem.setPrice(price);
        orderItem.setPreTaxPrice(preTaxPrice);
        return orderItem;
    }

    private BigDecimal getProductTotalPrice(CartItemDto cartItem) {
        ProductResponse product = cartItem.getProduct();
        BigDecimal productPrice = product.getPrice().setScale(SCALE, ROUNDING);
        BigDecimal totalItemPrice = productPrice;

        CategoryResponse category = categoryService.getCategory(product.getCategoryId());
        List<CategoryTaxResponse> categoryTaxResponse = category.getTaxes();
        List<TaxDetailDto> taxDetails = taxService.calculateTaxBreakdown(productPrice, categoryTaxResponse);

        for (TaxDetailDto taxDetail : taxDetails) {
            totalItemPrice = totalItemPrice.add(
                    taxDetail.getAmount()
            );
        }
        return totalItemPrice.setScale(SCALE, ROUNDING);
    }

}
