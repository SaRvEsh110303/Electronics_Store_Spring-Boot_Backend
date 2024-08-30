package com.sarz.electronic.store.Service;

import com.sarz.electronic.store.dtos.ProductDTO;
import com.sarz.electronic.store.dtos.pageableResponse;

public interface ProductService {
    //CREATE
    ProductDTO CreateProduct(ProductDTO productDTO);
    //UPDATE
    ProductDTO updateProduct(ProductDTO productDTO, String prodId);
    //DELETE
    void deleteProduct(String prodId);
    //GET SINGLE
    ProductDTO getSingleId(String prodId);
    // GET ALL
    pageableResponse<ProductDTO> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    // GET ALL : LIVE
    pageableResponse<ProductDTO> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);
    // Search Product
    pageableResponse<ProductDTO> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);


    //CREATE PRODUCT WITH CATEGORY
    ProductDTO createWithCategory(ProductDTO productDTO, String categoryId);

    // ASSIGN CATEGORY INTO THE PRODUCT
    ProductDTO updateCategory(String categoryId, String productId);

    // RETURNS PRODUCT OF GIVEN CATEGORY
    pageableResponse<ProductDTO>getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
}
