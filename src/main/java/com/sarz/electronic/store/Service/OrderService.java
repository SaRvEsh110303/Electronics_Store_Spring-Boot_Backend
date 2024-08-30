package com.sarz.electronic.store.Service;

import com.sarz.electronic.store.dtos.CreateOrderRequest;
import com.sarz.electronic.store.dtos.OrderDTO;
import com.sarz.electronic.store.dtos.pageableResponse;

import java.util.List;

public interface OrderService {

    // CREATE ORDER
    OrderDTO createOrder(CreateOrderRequest orderDTO);
    //REMOVE ORDER
    void removeOrder(String orderId);
    // GET ORDERS OF USER
    List<OrderDTO> getOrderOfUser(String userId);
    //GET ORDERS
    pageableResponse<OrderDTO> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
    //ORDER METHODS(LOGIC) RELATED TO ORDER

    OrderDTO updateOrder(CreateOrderRequest orderDTO, String orderId);
}
