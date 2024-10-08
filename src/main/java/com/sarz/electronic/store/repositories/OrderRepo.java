package com.sarz.electronic.store.repositories;

import com.sarz.electronic.store.entities.Order;
import com.sarz.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,String>
{

    List<Order> findByUser(User user);
}
