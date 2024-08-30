package com.sarz.electronic.store.controllers;

import com.sarz.electronic.store.Service.FileService;
import com.sarz.electronic.store.Service.ProductService;
import com.sarz.electronic.store.dtos.APIResponseMessage;
import com.sarz.electronic.store.dtos.ImageResponse;
import com.sarz.electronic.store.dtos.ProductDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/products")
@Tag(name ="Product Controllers",description ="This is for products")
@SecurityRequirement(name = "Scheme1")

public class ProductControllers {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;
    //CREATE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create New Product ")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO){
        ProductDTO productDTO1 = productService.CreateProduct(productDTO);
        return new ResponseEntity<>(productDTO1, HttpStatus.CREATED);
    }
    //UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{prodId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,@PathVariable("prodId") String prodId){
        ProductDTO productDTO1 = productService.updateProduct(productDTO, prodId);
        return new ResponseEntity<>(productDTO1,HttpStatus.CREATED);
    }
    //DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{prodId}")
    public ResponseEntity<APIResponseMessage> deleteProduct(@PathVariable("prodId") String prodId){
        productService.deleteProduct(prodId);
        APIResponseMessage message = APIResponseMessage.builder()
                .message("Product is Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    // GET SINGLE ID
    @GetMapping("/{prodId}")
    public ResponseEntity<ProductDTO> getSingleId(@PathVariable("prodId")String prodId){
        ProductDTO singleId = productService.getSingleId(prodId);
        return new ResponseEntity<>(singleId,HttpStatus.OK);
    }
    //GET ALL ID
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<pageableResponse<ProductDTO>> getAllId(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDTO> all = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
    // Get Live
    @GetMapping("/live")
    public ResponseEntity<pageableResponse<ProductDTO>> getAllLive(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDTO> allLive = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allLive,HttpStatus.OK);
    }
    // Get Product Title
    @GetMapping("/search/{query}")
    public ResponseEntity<pageableResponse<ProductDTO>>getTitle(
            @PathVariable("query")String query,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        pageableResponse<ProductDTO> productDTOpageableResponse = productService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOpageableResponse,HttpStatus.OK);
    }
    //UPLOAD IMAGE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable String productId,
            @RequestParam("productImage")MultipartFile image
            ) throws IOException {
        String uploadImage = fileService.uploadImage(image, imagePath);
        ProductDTO productDTO = productService.getSingleId(productId);
        productDTO.setProductImageName(uploadImage);
        ProductDTO newUpdate = productService.updateProduct(productDTO, productId);
        ImageResponse response = ImageResponse.builder().ImageName(newUpdate.getProductImageName()).message("Product Image uploaded Successfully!!").success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    // SERVE IMAGE
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/image/{productId}")
    public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDTO singleId = productService.getSingleId(productId);
        InputStream resource = fileService.getResource(imagePath, singleId.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
