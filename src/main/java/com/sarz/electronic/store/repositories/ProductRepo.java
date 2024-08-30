package com.sarz.electronic.store.repositories;

import com.sarz.electronic.store.entities.Category;
import com.sarz.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(Pageable pageable, String subTitle);
    Page<Product> findByLiveTrue(Pageable Live);
    Page<Product> findByCategory(Category category , Pageable pageable);

    //CUSTOM FINDER
    //QUERIES

}
