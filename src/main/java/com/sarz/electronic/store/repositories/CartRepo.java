package com.sarz.electronic.store.repositories;

import com.sarz.electronic.store.entities.Cart;
import com.sarz.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
