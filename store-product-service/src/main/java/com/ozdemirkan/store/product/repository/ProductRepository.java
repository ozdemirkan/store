package com.ozdemirkan.store.product.repository;

import com.ozdemirkan.store.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    Map<String, Product> productList = new HashMap<>();

    public Optional<Product> findByName(String name){
        return productList.entrySet()
                .stream()
                .filter(productEntry -> productEntry.getValue().getName().equalsIgnoreCase(name.toLowerCase()))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    public void save(Product product){
        productList.put(product.getId(),product);
    }

    public Optional<List<Product>> findAllByName(String name) {
        List<Product> products ;
        if(name ==null){
            products = new ArrayList<>(productList.values());
        }else {
            products = productList.entrySet()
                    .stream()
                    .filter(productEntry -> productEntry.getValue().getName().toLowerCase().contains(name.toLowerCase()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }

        return Optional.of(products);
    }
}
