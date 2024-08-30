package com.sarz.electronic.store.controllers;

import com.sarz.electronic.store.Service.CartService;
import com.sarz.electronic.store.dtos.APIResponseMessage;
import com.sarz.electronic.store.dtos.AddItemToCart;
import com.sarz.electronic.store.dtos.CartDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name ="Cars Controllers",description ="This is for Carts")
@SecurityRequirement(name = "Scheme1")
public class CartControllers {
    @Autowired
    private CartService cartService;

    //add items to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable String userId,@RequestBody AddItemToCart request){
        CartDTO cartDTO = cartService.addItemToCart(userId, request);
         return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<APIResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int itemId){
        cartService.removeItemFromCart(userId,itemId);
        APIResponseMessage message = APIResponseMessage.builder()
                .message("Item is removed from the cart !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<APIResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        APIResponseMessage message = APIResponseMessage.builder()
                .message("Cart Is Clear !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable String userId){
        CartDTO cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser,HttpStatus.OK);
    }


}
