package com.sarz.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDTO {

    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOT-PAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate=new Date();
    private Date deliveryDate;
//    private UserDTO user;
    private List<OrderItemDTO> orderItemList = new ArrayList<>();

}
