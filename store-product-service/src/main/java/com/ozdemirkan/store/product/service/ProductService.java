package com.ozdemirkan.store.product.service;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.exception.ErrorType;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) throws BusinessException {

        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new BusinessException(ErrorType.UNPROCCESSABLE_ENTITY);
        }

        Product newProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .createDate(Timestamp.valueOf(LocalDateTime.now())).build();

        productRepository.save(newProduct);
        return newProduct;
    }

}
