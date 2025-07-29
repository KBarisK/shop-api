package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import com.staj.gib.shopapi.entity.Product;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.CartDto;
import com.staj.gib.shopapi.enums.CartStatus;
import com.staj.gib.shopapi.exception.CartItemNotFoundExcepiton;
import com.staj.gib.shopapi.exception.CartNotFoundException;
import com.staj.gib.shopapi.exception.ProductNotFoundException;
import com.staj.gib.shopapi.exception.UserNotFoundException;
import com.staj.gib.shopapi.repository.CartItemRepository;
import com.staj.gib.shopapi.repository.CartRepository;
import com.staj.gib.shopapi.repository.ProductRepository;
import com.staj.gib.shopapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            return new CartDto( cart.getId(),
                    cart.getCreatedAt(),
                    cart.getUpdatedAt(),
                    cart.getCartItems() //TODO change it to cartItemDTO
            );
        }
        else{
            User  user = this.userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
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
                .orElseThrow(() -> new ProductNotFoundException(productId));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

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
                .orElseThrow(CartItemNotFoundExcepiton::new);
        this.cartItemRepository.delete(cartItem);
    }

    public void removeAllItemsFromCart(UUID cartId) {
        List<CartItem> cartItemList = this.cartItemRepository.findAllByCart_Id(cartId);
        this.cartItemRepository.deleteAll(cartItemList);
    }


}
