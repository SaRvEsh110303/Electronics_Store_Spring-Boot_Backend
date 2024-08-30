package com.sarz.electronic.store.Service;

import com.sarz.electronic.store.dtos.CategoryDTO;
import com.sarz.electronic.store.dtos.pageableResponse;

public interface CategoryService {
    CategoryDTO create(CategoryDTO categoryDTO);
    CategoryDTO update(CategoryDTO categoryDTO, String userId);
    void delete(String userId);
    pageableResponse<CategoryDTO> getAllId(int pageNumber,int pageSize,String sortBy, String sortDir);
    CategoryDTO getSingleId(String userId);
}
