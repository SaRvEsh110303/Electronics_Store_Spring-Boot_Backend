package com.sarz.electronic.store.repositories;

import com.sarz.electronic.store.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,String> {
}
