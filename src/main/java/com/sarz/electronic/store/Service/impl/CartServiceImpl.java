package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.Service.CartService;
import com.sarz.electronic.store.dtos.AddItemToCart;
import com.sarz.electronic.store.dtos.CartDTO;
import com.sarz.electronic.store.entities.Cart;
import com.sarz.electronic.store.entities.CartItemQuantity;
import com.sarz.electronic.store.entities.Product;
import com.sarz.electronic.store.entities.User;
import com.sarz.electronic.store.exceptions.BadApiRequest;
import com.sarz.electronic.store.exceptions.ResourceNotFoundException;
import com.sarz.electronic.store.repositories.CartItemRepo;
import com.sarz.electronic.store.repositories.CartRepo;
import com.sarz.electronic.store.repositories.ProductRepo;
import com.sarz.electronic.store.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CartDTO addItemToCart(String userId, AddItemToCart request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if (quantity <= 0) {
            throw new BadApiRequest("Quantity less than 0");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Id not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Id not found"));

        Cart cart = null;
        try {
            cart = cartRepo.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        List<CartItemQuantity> items = cart.getItems();
        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                // item already exists in the cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedList);

        if (!updated.get()) {
            CartItemQuantity cartItem = CartItemQuantity.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepo.save(cart);
        return mapper.map(updatedCart, CartDTO.class);
    }

    @Override
    public void removeItemFromCart(String userId, int CartItem) {
        CartItemQuantity cartItemQuantity = cartItemRepo.findById(CartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found!!"));
        cartItemRepo.delete(cartItemQuantity);

    }

    @Override
    public void clearCart(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID not found!!"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User Not Found !!"));
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    @Override
    public CartDTO getCartByUser(String userId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID not Found!!"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User Not Found!!"));
        return mapper.map(cart,CartDTO.class);
    }
}
