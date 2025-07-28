package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.CartDto;
import com.staj.gib.shopapi.enums.CartStatus;
import com.staj.gib.shopapi.exception.CartItemNotFound;
import com.staj.gib.shopapi.exception.CartNotFound;
import com.staj.gib.shopapi.exception.ProductNotFound;
import com.staj.gib.shopapi.exception.UserNotFoundException;
import com.staj.gib.shopapi.repository.CartItemRepository;
import com.staj.gib.shopapi.repository.CartRepository;
import com.staj.gib.shopapi.repository.ProductRepository;
import com.staj.gib.shopapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartDto getActiveCart(UUID userId) {
        Optional<Cart> optionalCart = this.cartRepository.findByUser_IdAndStatus(userId, CartStatus.OPEN);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<CartItem> cartItemList = this.cartItemRepository.findAllByCart_Id(cart.getId());
            return new CartDto(cart, cartItemList);
        }
        else{
            User  user = this.userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
            Cart newCart = new Cart(user,CartStatus.OPEN);
            Cart savedCart = this.cartRepository.save(newCart);
            List<CartItem> cartItemList = this.cartItemRepository.findAllByCart_Id(savedCart.getId());
            return new  CartDto(savedCart, cartItemList);
        }

    }

    public void addItemToCart(UUID cartId, UUID productId,short quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFound(productId));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFound(cartId));

        Optional<CartItem> existing = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity((short) (item.getQuantity() + quantity));
            cartItemRepository.save(item);
        } else {
            CartItem cartItem = new CartItem(cart, product, product.getPrice(), quantity);
            cartItemRepository.save(cartItem);
        }
    }

    public void removeItemFromCart(UUID cartId, UUID productId) {
        CartItem cartItem = this.cartItemRepository.findByCart_IdAndProduct_Id(cartId,productId)
                .orElseThrow(CartItemNotFound::new);
        this.cartItemRepository.delete(cartItem);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        List<CartItem> cartItemList = this.cartItemRepository.findAllByCart_Id(cartId);
        this.cartItemRepository.deleteAll(cartItemList);
    }


}
