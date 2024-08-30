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
public class CartDTO {
    private String CartId;
    private Date createdAt;
    private UserDTO user;
    private List<CartItemDTO> items=new ArrayList<>();
}
