package com.sarz.electronic.store.controllers;

import com.sarz.electronic.store.Service.FileService;
import com.sarz.electronic.store.Service.UserService;
import com.sarz.electronic.store.dtos.APIResponseMessage;
import com.sarz.electronic.store.dtos.ImageResponse;
import com.sarz.electronic.store.dtos.UserDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller",description = "This is the cart for the user " )
@SecurityRequirement(name = "Scheme1")
public class UserControllers {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    private Logger logger= LoggerFactory.getLogger(UserControllers.class);

    //CREATE
    @PostMapping
    @Operation(summary = "Create New User!!",description = "This is the user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "SUCCESS | OK"),
            @ApiResponse(responseCode = "401",description = "Not Authorized"),
            @ApiResponse(responseCode = "201",description = "New User Created")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO userDTO1 = userService.CreateUser(userDTO);
        return new ResponseEntity<>(userDTO1, HttpStatus.CREATED);
    }

    //UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") String userId, @RequestBody UserDTO userDTO){
        UserDTO userDTO1 = userService.updateUser(userDTO, userId);
        return new ResponseEntity<>(userDTO1,HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponseMessage> deleteUser(@PathVariable("userId") String userId) throws IOException {
    userService.deleteUser(userId);
    APIResponseMessage message = APIResponseMessage.builder().message("User is deleted successfully")
            .success(true)
            .status(HttpStatus.OK)
            .build();
    return new ResponseEntity<>(message,HttpStatus.OK);
}

    //GET ALL
    @GetMapping
    @Operation(summary = "Get Users")
    public ResponseEntity<pageableResponse<UserDTO>> getAllUsers(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    ){
    return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);

    }

    //GET SINGLE

    @GetMapping("/{userId}")
    @Operation(summary = "Get Single User")
    public ResponseEntity<UserDTO> getSingle(@PathVariable("userId") String userId){
    return new ResponseEntity<>(userService.getId(userId),HttpStatus.OK);
    }

    //GET BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getEmail(@PathVariable String email){
    return new ResponseEntity<>(userService.getEmail(email),HttpStatus.OK);
    }
    //SEARCH USER

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDTO>> getUser(@PathVariable String keyword){
    return new ResponseEntity<>(userService.searchUser(keyword),HttpStatus.OK);
    }

    //UPLOAD
    @PostMapping("/image/{userId}")

    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String ImageName = fileService.uploadImage(image, imageUploadPath);
        //Update
        UserDTO user = userService.getId(userId);
        user.setImageName(ImageName);
        UserDTO userDTO = userService.updateUser(user, userId);

        ImageResponse imageResponse= ImageResponse.builder().ImageName(ImageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    //SERVE USER IMAGE
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDTO user = userService.getId(userId);
        logger.info("User Image Name : {}",user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
