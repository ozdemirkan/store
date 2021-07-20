package com.ozdemirkan.store.product.service;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.exception.ErrorType;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.GetProductDetailsResponse;
import com.ozdemirkan.store.product.model.UpdateProductRequest;
import com.ozdemirkan.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) throws BusinessException {

        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new BusinessException(ErrorType.UNPROCCESSABLE_ENTITY);
        }

        Product newProduct = Product.builder().name(request.getName()).price(request.getPrice()).currency(request.getCurrency()).build();

        productRepository.save(newProduct);
        return newProduct;
    }

    public Page<Product> findAllProductsByName(String name, Pageable pageable) {
        if(name==null || name.isEmpty()){
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public List<Product> findAllProductsByIds(List<String> ids) {
            return (List<Product>) productRepository.findAllById(ids);
    }

    public List<GetProductDetailsResponse.ProductDetail> getProductDetails (List<String> ids) {
        List<Product> products = (List<Product>) productRepository.findAllById(ids);
        return products.stream()
                .map(product -> new GetProductDetailsResponse.ProductDetail(product.getId(), product.getPrice(), product.getCurrency()))
                .collect(Collectors.toList());
    }

    @Transactional
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
