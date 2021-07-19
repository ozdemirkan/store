package com.ozdemirkan.store.product.repository;

import com.ozdemirkan.store.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository {

    Map<String, Product> productList = new HashMap<>();

    public Optional<Product> findByName(String name){
        return Optional.ofNullable(productList.get(name));
    }

    public void save(Product product){
        productList.put(product.getName(),product);
    }
}
