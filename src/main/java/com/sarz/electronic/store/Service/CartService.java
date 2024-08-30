package com.sarz.electronic.store.Service;

import com.sarz.electronic.store.dtos.AddItemToCart;
import com.sarz.electronic.store.dtos.CartDTO;

public interface CartService {


    //ADD ITEMS TO THE CART
    // case1 : cart banaao agar cart nhi hai toh item daalne se pehle
    // case2 : item ko add karte rte rho ek hi cart me
    CartDTO addItemToCart(String userId, AddItemToCart request);

    //Remove items from the cart
    void removeItemFromCart(String userId,int CartItem);

    // REMOVE ALL ITEMS FROM THE CART
    void clearCart(String userId);

    CartDTO getCartByUser(String userId);

}
