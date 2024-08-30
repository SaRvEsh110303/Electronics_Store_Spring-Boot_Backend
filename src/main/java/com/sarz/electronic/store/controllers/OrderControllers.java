package com.sarz.electronic.store.controllers;

import com.sarz.electronic.store.Service.OrderService;
import com.sarz.electronic.store.dtos.APIResponseMessage;
import com.sarz.electronic.store.dtos.CreateOrderRequest;
import com.sarz.electronic.store.dtos.OrderDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name ="Order Controllers",description ="This is for orders")
@SecurityRequirement(name = "Scheme1")

public class OrderControllers {
    @Autowired
    public OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest orderDTO){
        OrderDTO order = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<APIResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        APIResponseMessage message = APIResponseMessage.builder()
                .message("Order is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrderOfUser(@PathVariable String userId){
        List<OrderDTO> orderOfUser = orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orderOfUser,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<pageableResponse> getOrders(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<OrderDTO> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> AdminUpdate(@RequestBody CreateOrderRequest orderDTO,@PathVariable String orderId){
        OrderDTO updateOrder = orderService.updateOrder(orderDTO, orderId);
        return new ResponseEntity<>(updateOrder,HttpStatus.OK);
    }
}
