package com.staj.gib.shopapi.entity.dto;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.entity.CartItem;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class CartDto implements Serializable {
    Cart cart;
    List<CartItem> cartItems;
}
