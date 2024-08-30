package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.Service.OrderService;
import com.sarz.electronic.store.dtos.CreateOrderRequest;
import com.sarz.electronic.store.dtos.OrderDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import com.sarz.electronic.store.entities.*;
import com.sarz.electronic.store.exceptions.ResourceNotFoundException;
import com.sarz.electronic.store.helper.Helper;
import com.sarz.electronic.store.repositories.CartRepo;
import com.sarz.electronic.store.repositories.OrderItemRepo;
import com.sarz.electronic.store.repositories.OrderRepo;
import com.sarz.electronic.store.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    public OrderRepo orderRepo;
    @Autowired
    public OrderItemRepo orderItemRepo;
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public CartRepo cartRepo;
    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDTO createOrder(CreateOrderRequest orderDTO) {
        String cartId = orderDTO.getCartId();
        String userId = orderDTO.getUserId();
        //fetch user
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Id not Found !!"));
        //fetch cart
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Id not Found !!"));

        List<CartItemQuantity> cartItem=cart.getItems();
        if(cartItem.size()<=0){
            throw new ResourceNotFoundException("Invalid Number of items in Cart !!");
        }
        //Other checks
        Order order = Order.builder()
                .billingName(orderDTO.getBillingName())
                .billingPhone(orderDTO.getBillingPhone())
                .billingAddress(orderDTO.getBillingAddress())
                .orderDate(new Date())
                .deliveryDate(null)
                .paymentStatus(orderDTO.getPaymentStatus())
                .orderStatus(orderDTO.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();


        // ORDER ITEMS, AMOUNT
        AtomicReference<Integer> orderAmount=new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItem.stream().map(cartItemQuantity -> {
//            cartItem  -> OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItemQuantity.getQuantity())
                    .product(cartItemQuantity.getProduct())
                    .totalPrice(cartItemQuantity.getQuantity() * cartItemQuantity.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItemList(orderItems);
        order.setOrderAmount(orderAmount.get());
        cart.getItems().clear();
        cartRepo.save(cart);
        Order SavedOrder = orderRepo.save(order);

        return mapper.map(SavedOrder,OrderDTO.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Id not Found !!"));
        orderRepo.delete(order);
    }

    @Override
    public List<OrderDTO> getOrderOfUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID not Found!!"));
        List<Order> orders = orderRepo.findByUser(user);
        List<OrderDTO> orderDTOS = orders.stream().map(order -> mapper.map(order, OrderDTO.class)).collect(Collectors.toList());

        return orderDTOS;
    }

    @Override
    public pageableResponse<OrderDTO> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepo.findAll(pageable);
        return Helper.getPageableResponse(page,OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(CreateOrderRequest orderDTO, String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Id not Found"));
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setDeliveryDate(generateRandomDeliveryDate(3,4));
        Order update = orderRepo.save(order);
        return mapper.map(update,OrderDTO.class);
    }

    private Date generateRandomDeliveryDate(int minDays, int maxDays) {
        // Get the current date
        long now = System.currentTimeMillis();

        // Calculate the minimum and maximum milliseconds for the delivery date range
        long minMillis = now + minDays * 24 * 60 * 60 * 1000L;
        long maxMillis = now + maxDays * 24 * 60 * 60 * 1000L;

        // Generate a random date within the range
        long randomMillis = ThreadLocalRandom.current().nextLong(minMillis, maxMillis);

        // Return the generated date
        return new Date(randomMillis);
    }
}
