package com.ozdemirkan.store.product.service;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.exception.ErrorType;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.UpdateProductRequest;
import com.ozdemirkan.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) throws BusinessException {

        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new BusinessException(ErrorType.UNPROCCESSABLE_ENTITY);
        }

        Product newProduct = Product.createInstance(request.getName(), request.getPrice(), request.getCurrency());

        productRepository.save(newProduct);
        return newProduct;
    }

    public Optional<List<Product>> findAllProductsByName(String name) {
        return productRepository.findAllByName(name);
    }

    public void updateProduct(String id, UpdateProductRequest updateProductRequest) throws BusinessException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new BusinessException(ErrorType.NOT_FOUND);
        }
        Product product = optionalProduct.get();
        product.setPrice(updateProductRequest.getPrice());
        product.setCurrency(updateProductRequest.getCurrency());
    }
}
