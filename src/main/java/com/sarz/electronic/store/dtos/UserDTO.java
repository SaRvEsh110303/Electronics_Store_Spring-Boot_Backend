package com.sarz.electronic.store.dtos;

import com.sarz.electronic.store.validation.ImageNameValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDTO {
    private String userId;
    @Size(min = 2,max=15,message = "Invalid Name!!")
    @Schema(name = "username",defaultValue = "user_name",accessMode = Schema.AccessMode.READ_ONLY,description = "User name of new User!!")
    private String name;
//    @Email(message = "Invalid User Email!!")
    @Pattern(regexp = "^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$",message = "Invalid Message!!")
    @NotBlank(message = "Email is Required!!")
    private String email;
    @NotBlank(message = "Password is Required!!")
    private String password;
    @Size(min = 4,max = 6,message = "Invalid Gender!!")
    private String gender;
    @NotBlank(message = "Write Something About Yourself!!")
    private String about;
    @ImageNameValid
    private String imageName;

    private Set<RolesDTO> roles=new HashSet<>();
}
