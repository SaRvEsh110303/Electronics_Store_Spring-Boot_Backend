package com.sarz.electronic.store.controllers;

import com.sarz.electronic.store.Service.CategoryService;
import com.sarz.electronic.store.Service.FileService;
import com.sarz.electronic.store.Service.ProductService;
import com.sarz.electronic.store.dtos.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
@RestController
@RequestMapping("/categories")
@Tag(name ="Category Controllers",description ="This is for categories")
@SecurityRequirement(name = "Scheme1")

public class CategoryControllers {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    @Value("${categories.image.path}")
    private String imagePath;
    //Create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> CreateCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO create = categoryService.create(categoryDTO);
        return new ResponseEntity<>(create, HttpStatus.CREATED);
    }
    //UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<CategoryDTO> UpdateCategory(@PathVariable("userId") String userId,@RequestBody CategoryDTO categoryDTO){
        CategoryDTO update = categoryService.update(categoryDTO, userId);
        return new ResponseEntity<>(update,HttpStatus.OK);
    }
    //DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponseMessage> deleteCategory(@PathVariable("userId") String userId){
        categoryService.delete(userId);
        APIResponseMessage message = APIResponseMessage.builder().
                message("User is Deleted Successfully!!").
                success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    //GET ALL ID
    @GetMapping
    public ResponseEntity<pageableResponse<CategoryDTO>>GetAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(categoryService.getAllId(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    // GET SINGLE ID
    @GetMapping("/{userId}")
    public ResponseEntity<CategoryDTO> getSingleId(@PathVariable String userId){
        CategoryDTO singleId = categoryService.getSingleId(userId);
        return new ResponseEntity<>(singleId,HttpStatus.OK);
    }

// CREATE PRODUCT WITH CATEGORY
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDTO> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDTO productDTO
    ){
        ProductDTO create = productService.createWithCategory(productDTO, categoryId);
        return new ResponseEntity<>(create,HttpStatus.CREATED);
    }

    // UPDATE CATEGORY OF PRODUCT
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDTO> updateCategoryOfProduct(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    ){
        ProductDTO updatedProductDTO = productService.updateCategory(categoryId, productId);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    // RETURNS PRODUCT OF GIVEN CATEGORY
    @GetMapping("{categoryId}/products")
    public ResponseEntity<pageableResponse<ProductDTO>> getAllProductFromCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDTO> allOfCategory = productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    // UPLOAD IMAGE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable String categoryId,
            @RequestBody MultipartFile image
            ) throws IOException {
        String uploadImage = fileService.uploadImage(image, imagePath);
        CategoryDTO categoryDTO = categoryService.getSingleId(categoryId);
        categoryDTO.setCoverName(uploadImage);
        CategoryDTO update = categoryService.update(categoryDTO, categoryId);
        ImageResponse response = ImageResponse.builder().ImageName(update.getCoverName()).message("Category Image Uploaded Successfully !!").success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    //Serve Image
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/image/{categoryId}")
    public void ServeImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDTO categoryDTO = categoryService.getSingleId(categoryId);
        InputStream resource = fileService.getResource(imagePath, categoryDTO.getCoverName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
