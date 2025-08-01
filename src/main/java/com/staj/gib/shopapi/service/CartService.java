package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.request.CartRepuest;
import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.dto.response.CategoryResponse;
import com.staj.gib.shopapi.dto.response.CategoryTaxResponse;
import com.staj.gib.shopapi.dto.response.ProductResponse;
import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.mapper.CartMapper;
import com.staj.gib.shopapi.enums.CartStatus;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.CartRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final TaxService taxService;
    private final CategoryService categoryService;
    private final ProductService productService;

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final EntityManager entityManager;


    public CartDto getActiveCart(UUID userId) {
        Optional<Cart> optionalCart = this.cartRepository.findByUser_IdAndStatus(userId, CartStatus.OPEN);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            return this.cartMapper.cartToCartDto(cart);
        }
        else{
            User user = this.entityManager.getReference(User.class,userId);
            Cart newCart = new Cart(user,CartStatus.OPEN,new ArrayList<>());
            Cart savedCart = this.cartRepository.save(newCart);
            return this.cartMapper.cartToCartDto(savedCart);
        }
    }

    public void addItemToCart(CartRepuest cartRepuest) {
        ProductResponse productResponse = productService.getProduct(cartRepuest.getProductId());
        Cart cart = cartRepository.findById(cartRepuest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartRepuest.getCartId()));

        Optional<CartItem> existingItem = cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(cartRepuest.getProductId())).findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            short newQuantity = (short) (item.getQuantity() + cartRepuest.getQuantity());
            item.setQuantity(newQuantity);
        } else {
            CategoryResponse categoryResponse = this.categoryService.getCategory(productResponse.getCategoryId());
            List<CategoryTaxResponse> taxes = categoryResponse.getTaxes();
            BigDecimal priceAfterTax = productResponse.getPrice();
            BigDecimal preTaxPrice = productResponse.getPrice();
            for(CategoryTaxResponse tax : taxes) {
                priceAfterTax = priceAfterTax.add(taxService.calculateTax(preTaxPrice, tax));
            }
            Product product = this.entityManager.getReference(Product.class,productResponse.getId());
            CartItem newItem = new CartItem(cart, product, priceAfterTax, preTaxPrice, cartRepuest.getQuantity());
            cart.getCartItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    public void removeItemFromCart(CartRepuest cartRepuest) {
        Cart cart = this.cartRepository.findById(cartRepuest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartRepuest.getCartId()));
        cart.getCartItems().removeIf(item -> { //true means delete false means keep the item.
            if (item.getProduct().getId().equals(cartRepuest.getProductId())) {
                short currentQuantity = item.getQuantity();
                if (cartRepuest.getQuantity() >= currentQuantity) {
                    return true;
                } else {
                    item.setQuantity((short) (currentQuantity - cartRepuest.getQuantity()));
                    return false;
                }
            }
            return false;
        });
        cartRepository.save(cart);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        Cart cart  = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        cart.setCartItems(new ArrayList<>());
        cartRepository.save(cart);
    }


}
