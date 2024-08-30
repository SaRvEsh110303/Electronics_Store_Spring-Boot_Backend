package com.sarz.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {
    @NotBlank(message = "Cart Id is required")
    private String cartId;
    @NotBlank(message = "User Id is required")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOT-PAID";
    @NotBlank( message = "Address is required")
    private String billingAddress;
    @NotBlank(message = "Phone number is required")
    private String billingPhone;
    @NotBlank(message = "Name is required")
    private String billingName;
}
