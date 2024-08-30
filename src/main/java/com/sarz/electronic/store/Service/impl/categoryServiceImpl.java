package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.Service.CategoryService;
import com.sarz.electronic.store.dtos.CategoryDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import com.sarz.electronic.store.entities.Category;
import com.sarz.electronic.store.exceptions.ResourceNotFoundException;
import com.sarz.electronic.store.helper.Helper;
import com.sarz.electronic.store.repositories.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class categoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        String userId= UUID.randomUUID().toString();
        categoryDTO.setCategoryId(userId);
        Category category = mapper.map(categoryDTO, Category.class);
        Category create = categoryRepo.save(category);
        CategoryDTO newDto = mapper.map(create, CategoryDTO.class);
        return newDto;
    }
    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, String userId) {
        Category update = categoryRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Id not Found"));
        update.setTitle(categoryDTO.getTitle());
        update.setDescription(categoryDTO.getDescription());
        update.setCoverName(categoryDTO.getCoverName());
        Category newUpdate = categoryRepo.save(update);
        CategoryDTO updateResponse = mapper.map(newUpdate, CategoryDTO.class);
        return updateResponse;
    }

    @Override
    public void delete(String userId) {
        Category category = categoryRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID not Found"));
        categoryRepo.delete(category);
    }

    @Override
    public pageableResponse<CategoryDTO> getAllId(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepo.findAll(pageable);
        pageableResponse<CategoryDTO> pageableResponse = Helper.getPageableResponse(page, CategoryDTO.class);
        return pageableResponse;
    }

    @Override
    public CategoryDTO getSingleId(String userId) {
        Category category = categoryRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Id not Found"));
        CategoryDTO getId = mapper.map(category, CategoryDTO.class);
        return getId;
    }
}
