package com.sarz.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDTO {
    private String productId;
    @NotBlank(message = "Product Name is Compulsory")
    private String productName;
    private String title;
    private String description;
    private int price;
    private int discountedPrice;
    private String quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
    private CategoryDTO category;

}
