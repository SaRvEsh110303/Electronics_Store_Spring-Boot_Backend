package com.sarz.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String ImageName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
