package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.CartDto;
import com.staj.gib.shopapi.enums.CartStatus;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.CartRepository;
import com.staj.gib.shopapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository; //TODO CHANGE IT TO SERVICE WHEN AVAILABLE
    private final CartRepository cartRepository;
    private final UserService userService;

    public CartDto getActiveCart(UUID userId) {
        Optional<Cart> optionalCart = this.cartRepository.findByUser_IdAndStatus(userId, CartStatus.OPEN);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            return new CartDto( cart.getId(),
                    cart.getCreatedAt(),
                    cart.getUpdatedAt(),
                    cart.getCartItems() //TODO change it to cartItemDTO
            );
        }
        else{
            User user = this.userService.getEntityById(userId);
            Cart newCart = new Cart(user,CartStatus.OPEN,new ArrayList<>());
            Cart savedCart = this.cartRepository.save(newCart);
            return new CartDto( savedCart.getId(),
                    savedCart.getCreatedAt(),
                    savedCart.getUpdatedAt(),
                    new ArrayList<>()
            );
        }

    }

    public void addItemToCart(UUID cartId, UUID productId,short quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",productId));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            short newQuantity = (short) (item.getQuantity() + quantity);
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem(cart, product, product.getPrice(), quantity);
            cart.getCartItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    public void removeItemFromCart(UUID cartId, UUID productId,short quantity) {
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getProduct().getId().equals(productId)) {
                short currentQuantity = cartItem.getQuantity();
                if (quantity >= currentQuantity) {
                    iterator.remove();
                } else {
                    cartItem.setQuantity((short) (currentQuantity - quantity));
                }
                break;
            }
        }
        cartRepository.save(cart);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        Cart cart  = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart",cartId));
        cart.setCartItems(new ArrayList<>());
        cartRepository.save(cart);
    }


}
