package com.sarz.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDTO {
    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDTO product;
}
