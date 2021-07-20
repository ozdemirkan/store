package com.ozdemirkan.store.product.repository;

import com.ozdemirkan.store.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {


    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Product> findByName(String name);

    Optional<Product> findById(String id);


}
