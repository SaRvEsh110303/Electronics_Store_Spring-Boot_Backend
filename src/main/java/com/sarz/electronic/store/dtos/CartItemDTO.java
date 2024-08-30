package com.sarz.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartItemDTO {
    private int CartItemId;
    private ProductDTO product;
    private int quantity;
    private int totalPrice;
}
