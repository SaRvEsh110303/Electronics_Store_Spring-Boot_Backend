package com.sarz.electronic.store.repositories;

import com.sarz.electronic.store.entities.CartItemQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItemQuantity,Integer> {
}
