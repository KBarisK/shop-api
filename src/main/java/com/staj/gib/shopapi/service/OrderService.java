package com.staj.gib.shopapi.service;

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

    private final CartService cartService;
    private final TaxService taxService;
    private final CategoryService categoryService;
    private final InstallmentPaymentService installmentPaymentService;
    private final ProductService productService;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        // 1. Validate and retrieve cart
        CartDto cart = validateAndGetCart(orderRequest.getCartId());
        
        // 2. Calculate total amount based on payment method
        BigDecimal totalAmount = calculateOrderTotal(cart.getCartItems(), orderRequest);

        // 3. Create and save initial order
        // TODO user needs to be set
        Order order = createInitialOrder(totalAmount, orderRequest.getPaymentMethod());
        
        // 4. Process cart items and update inventory
        List<OrderItem> orderItems = processCartItems(cart.getCartItems(), order);
        order.setOrderItems(orderItems);
        
        // 5. Handle payment method specific logic
        handlePaymentMethod(order, orderRequest);
        
        // 6. Save final order and clear cart
        Order savedOrder = orderRepository.save(order);
        cartService.removeAllItemsFromCart(orderRequest.getCartId());
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    private CartDto validateAndGetCart(UUID cartId) {
        CartDto cart = cartService.getCart(cartId);
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty, cannot place order");
        }
        return cart;
    }

    private BigDecimal calculateOrderTotal(List<CartItemDto> cartItems, OrderRequest orderRequest) {
        if (orderRequest.getPaymentMethod() == PaymentMethod.PAYMENT_INSTALLMENT) {
            validateInstallmentRequest(orderRequest);
            return calculateTotalAmountWithInterest(cartItems, orderRequest.getInstallmentCount());
        }
        return calculateTotalAmount(cartItems);
    }

    private void validateInstallmentRequest(OrderRequest orderRequest) {
        if (orderRequest.getInstallmentCount() <= 0) {
            throw new IllegalArgumentException("Installment count must be greater than 0");
        }
        if (orderRequest.getInterestRate() == null || orderRequest.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate must be non-negative");
        }
    }

    private Order createInitialOrder(BigDecimal totalAmount, PaymentMethod paymentMethod) {
        Order order = new Order();
        order.setStatus(determineInitialOrderStatus(paymentMethod));
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setOrderItems(new ArrayList<>());
        return orderRepository.save(order);
    }

    private OrderStatus determineInitialOrderStatus(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.PAYMENT_CASH ? OrderStatus.FINISHED : OrderStatus.ACTIVE;
    }

    private List<OrderItem> processCartItems(List<CartItemDto> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> {
                    validateCartItem(cartItem);
                    OrderItem orderItem = createOrderItem(cartItem);
                    orderItem.setOrder(order);
                    productService.decrementStock(cartItem.getProduct().getId(), cartItem.getQuantity());
                    return orderItem;
                })
                .toList();
    }

    private void validateCartItem(CartItemDto cartItem) {
        if (cartItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Cart item quantity must be greater than 0");
        }
        if (cartItem.getProduct() == null) {
            throw new IllegalArgumentException("Cart item must have a valid product");
        }
    }

    private void handlePaymentMethod(Order order, OrderRequest orderRequest) {
        switch (orderRequest.getPaymentMethod()) {
            case PAYMENT_INSTALLMENT -> handleInstallmentPayment(order, orderRequest);
            case PAYMENT_CASH -> handleCashPayment(order);
            default -> throw new IllegalArgumentException("Unsupported payment method: " + orderRequest.getPaymentMethod());
        }
    }

    private void handleInstallmentPayment(Order order, OrderRequest orderRequest) {
        installmentPaymentService.saveInstallmentPayment(
                order, 
                orderRequest.getInterestRate(), 
                orderRequest.getInstallmentCount()
        );
    }

    private void handleCashPayment(Order order) {
        CashPayment cashPayment = new CashPayment();
        cashPayment.setOrder(order);
        order.setCashPayment(cashPayment);
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", orderId));
        return this.orderMapper.toOrderResponse(order);
    }

    private BigDecimal calculateTotalAmountWithInterest(List<CartItemDto> cartItems, int installmentCount){
        BigDecimal totalAmount = calculateTotalAmount(cartItems);

        // Add interest
        BigDecimal monthlyRate = BigDecimal.valueOf(0.0425);

        // Annuity calculation: P × [r_eff × (1 + r_eff)^n] / [(1 + r_eff)^n – 1]
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRatePowerN = onePlusRate.pow(installmentCount);

        // Numerator: r_eff × (1 + r_eff)^n
        BigDecimal numerator = monthlyRate.multiply(onePlusRatePowerN);

        // Denominator: (1 + r_eff)^n – 1
        BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);

        // Monthly installment amount
        BigDecimal monthlyInstallment = totalAmount.multiply(
                numerator.divide(denominator, SCALE, ROUNDING)
        );

        // Total amount to be paid
        return monthlyInstallment.multiply(BigDecimal.valueOf(installmentCount));
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
