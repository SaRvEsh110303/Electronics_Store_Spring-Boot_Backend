package com.sarz.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;
    //pending, dispatched, delivered
    private String orderStatus;
    //NOT PAID, PAID
    //boolean -> False => Not Paid, True => Paid
    private String paymentStatus;
    private int orderAmount;
    @Column(length = 5000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate;
    private Date deliveryDate;

    //User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> orderItemList = new ArrayList<>();
    
}
