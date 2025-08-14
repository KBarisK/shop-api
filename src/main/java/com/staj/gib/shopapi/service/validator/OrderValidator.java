package com.staj.gib.shopapi.service.validator;

import com.staj.gib.shopapi.constant.RoundingConstants;
import com.staj.gib.shopapi.dto.mapper.OrderMapper;
import com.staj.gib.shopapi.dto.response.*;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.entity.OrderItem;
import com.staj.gib.shopapi.enums.PaymentMethod;
import com.staj.gib.shopapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final CartService cartService;
    private final ProductService productService;
    private final CashPaymentService cashPaymentService;
    private final InstallmentPaymentService installmentPaymentService;
    private final OrderMapper orderMapper;


    public CartOrderDto validateAndGetCart(UUID cartId) {
        CartOrderDto cart = cartService.getCart(cartId);
        if (Objects.isNull(cart.getCartItems())|| cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty, cannot place order");
        }
        return cart;
    }

    public BigDecimal calculateOrderTotal(List<CartItemDto> cartItems,
                                          PaymentMethod paymentMethod, int installmentCount) {
        if (paymentMethod == PaymentMethod.PAYMENT_INSTALLMENT) {
            return calculateTotalAmountWithInterest(cartItems, installmentCount);
        }
        return calculateTotalAmount(cartItems);
    }

    public void handlePaymentMethod(Order order, PaymentMethod paymentMethod, int installmentCount) {
        switch (paymentMethod) {
            case PAYMENT_INSTALLMENT -> handleInstallmentPayment(order, installmentCount);
            case PAYMENT_CASH -> handleCashPayment(order);
            default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
    }

    public List<OrderItem> processCartItems(List<CartItemDto> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> {
                    validateCartItem(cartItem);
                    OrderItem orderItem = createOrderItem(cartItem);
                    orderItem.setOrder(order);
                    productService.decrementStock(cartItem.getProduct().getId(), cartItem.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList()); // do not return an immutable list which JPA can't handle
    }

    private void validateCartItem(CartItemDto cartItem) {
        if (cartItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Cart item quantity must be greater than 0");
        }
        if (cartItem.getProduct() == null) {
            throw new IllegalArgumentException("Cart item must have a valid product");
        }
    }


    private void handleInstallmentPayment(Order order, int installmentCount) {
        installmentPaymentService.saveInstallmentPayment(
                orderMapper.toOrderResponse(order),
                RoundingConstants.DEFAULT_MONTHLY_INTEREST_RATE,
                installmentCount
        );
    }

    private void handleCashPayment(Order order) {
        cashPaymentService.handleCashPayment(order.getId());
    }


    private BigDecimal calculateTotalAmountWithInterest(List<CartItemDto> cartItems, int installmentCount){
        BigDecimal totalAmount = calculateTotalAmount(cartItems);

        BigDecimal monthlyRate = RoundingConstants.DEFAULT_MONTHLY_INTEREST_RATE;

        // Annuity calculation: P × [r_eff × (1 + r_eff)^n] / [(1 + r_eff)^n – 1]
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRatePowerN = onePlusRate.pow(installmentCount);

        // Numerator: r_eff × (1 + r_eff)^n
        BigDecimal numerator = monthlyRate.multiply(onePlusRatePowerN);

        // Denominator: (1 + r_eff)^n – 1
        BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);

        BigDecimal monthlyInstallment = totalAmount.multiply(
                numerator.divide(denominator, RoundingConstants.SCALE, RoundingConstants.ROUNDING)
        );

        return monthlyInstallment.multiply(BigDecimal.valueOf(installmentCount));
    }

    private BigDecimal calculateTotalAmount(List<CartItemDto> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItemDto cartItem : cartItems) {
            BigDecimal totalItemPrice = getProductTotalPrice(cartItem);

            BigDecimal itemTotalWithQuantity = totalItemPrice
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                    .setScale(RoundingConstants.SCALE, RoundingConstants.ROUNDING);

            totalPrice = totalPrice.add(itemTotalWithQuantity);
        }
        return totalPrice.setScale(RoundingConstants.SCALE, RoundingConstants.ROUNDING);
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
        return productService.calculateAfterTaxPrice(cartItem.getProduct().getId());
    }
}
