package com.sarz.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private String categoryId;
    @NotBlank
    @Size(min = 4, message = "Title must be of minimum 4 characters")
    private String title;
    @NotBlank(message = "Description Required!!")
    private String description;
    @NotBlank(message = "Cover Name Required!!")
    private String coverName;
}
