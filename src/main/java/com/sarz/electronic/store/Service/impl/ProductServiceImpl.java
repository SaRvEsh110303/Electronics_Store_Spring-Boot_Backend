package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.Service.ProductService;
import com.sarz.electronic.store.dtos.ProductDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import com.sarz.electronic.store.entities.Category;
import com.sarz.electronic.store.entities.Product;
import com.sarz.electronic.store.exceptions.ResourceNotFoundException;
import com.sarz.electronic.store.helper.Helper;
import com.sarz.electronic.store.repositories.CategoryRepo;
import com.sarz.electronic.store.repositories.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String imagePath;
    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public ProductDTO CreateProduct(ProductDTO productDTO) {
        Product product = mapper.map(productDTO, Product.class);
        String prodId= UUID.randomUUID().toString();
        product.setProductId(prodId);
        //Added Date
        product.setAddedDate(new Date());
        Product create = productRepo.save(product);
        ProductDTO newDTO= mapper.map(create, ProductDTO.class);
        return newDTO;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, String prodId) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        product.setProductName(productDTO.getProductName());
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setLive(productDTO.isLive());
        product.setPrice(productDTO.getPrice());
        product.setDiscountedPrice(productDTO.getDiscountedPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStock(productDTO.isStock());
        product.setProductImageName(productDTO.getProductImageName());
        Product save = productRepo.save(product);
        ProductDTO update = mapper.map(save, ProductDTO.class);
        return update;
    }
    @Override
    public void deleteProduct(String prodId) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("Product Id is not found!!"));
        // DELETE IMAGE OF THE PRODUCT
        String fullPath= imagePath + product.getProductImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        productRepo.delete(product);
    }

    @Override
    public ProductDTO getSingleId(String prodId) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResourceNotFoundException("User Id is not Found!!"));
        ProductDTO getOneId = mapper.map(product, ProductDTO.class);
        return getOneId;
    }

    @Override
    public pageableResponse<ProductDTO> getAll(int pageNumber, int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()): (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepo.findAll(pageable);
        pageableResponse<ProductDTO> pageableResponse = Helper.getPageableResponse(page, ProductDTO.class);
        return pageableResponse;
    }

    @Override
    public pageableResponse<ProductDTO> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()): (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> byLiveTrue = productRepo.findByLiveTrue(pageable);
        pageableResponse<ProductDTO> pageableResponse = Helper.getPageableResponse(byLiveTrue, ProductDTO.class);
        return pageableResponse;
    }

    @Override
    public pageableResponse<ProductDTO> searchByTitle(String subTitle,int pageNumber, int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> byTitleContaining = productRepo.findByTitleContaining(pageable, subTitle);
        pageableResponse<ProductDTO> pageableResponse = Helper.getPageableResponse(byTitleContaining, ProductDTO.class);
        return pageableResponse;
    }

    @Override
    public ProductDTO createWithCategory(ProductDTO productDTO, String categoryId) {
        //FETCH THE CATEGORY FROM THE DATABASES;
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Id not found"));
        Product product = mapper.map(productDTO, Product.class);
        String prodId = UUID.randomUUID().toString();
        product.setProductId(prodId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product save = productRepo.save(product);
        ProductDTO create = mapper.map(save, ProductDTO.class);
        return create;
    }
    public ProductDTO updateCategory(String categoryId, String productId){
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Id not found !!"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Id not found !!"));
        product.setCategory(category);
        Product save = productRepo.save(product);
        return mapper.map(save,ProductDTO.class);
    }

    @Override
    public pageableResponse<ProductDTO> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category ID is not found!!"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page =productRepo.findByCategory(category,pageable);
        pageableResponse<ProductDTO> pageableResponse = Helper.getPageableResponse(page, ProductDTO.class);
        return pageableResponse;
    }
}
