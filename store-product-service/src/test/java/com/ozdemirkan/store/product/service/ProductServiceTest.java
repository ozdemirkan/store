package com.ozdemirkan.store.product.service;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.Currency;
import com.ozdemirkan.store.product.model.UpdateProductRequest;
import com.ozdemirkan.store.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
    }

    //CREATE PRODUCT//
    @Test
    void createProductSuccess() throws BusinessException {
        //given
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12", BigDecimal.valueOf(700.00), Currency.EUR);
        //when
        when(productRepository.findByName(createProductRequest.getName())).thenReturn(Optional.empty());
        //then
        Product newProduct = productService.createProduct(createProductRequest);
        assertEquals(newProduct.getName(), createProductRequest.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createExistingProductFails() throws BusinessException {
        //given
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12", BigDecimal.valueOf(700.00), Currency.EUR);
        //when
        when(productRepository.findByName(createProductRequest.getName()))
                .thenReturn(Optional.of(Product.builder().name("Iphone 12").price(BigDecimal.valueOf(700.00)).currency(Currency.EUR).build()));
        //then
        BusinessException exception = assertThrows(BusinessException.class, () -> productService.createProduct(createProductRequest));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getErrorType().getHttpStatus());

        verify(productRepository).findByName(createProductRequest.getName());
        verifyNoMoreInteractions(productRepository);
    }

    //FIND PRODUCT//
    @Test
    void findAllProductsByNameSuccess() {
        //given
        List<Product> products = List.of(
                Product.builder().name("Iphone 12 Mini").price(BigDecimal.valueOf(700.00)).currency(Currency.EUR).build(),
                Product.builder().name("Iphone 12").price(BigDecimal.valueOf(800.00)).currency(Currency.EUR).build(),
                Product.builder().name("Iphone 12 Ultra").price(BigDecimal.valueOf(900.00)).currency(Currency.EUR).build(),
                Product.builder().name("Iphone 12 Ultra Max").price(BigDecimal.valueOf(1000.00)).currency(Currency.EUR).build()
        );
        String productToBeSearched = "Iphone 12";

        //when
        when(productRepository.findByNameContainingIgnoreCase(productToBeSearched, PageRequest.of(0, 20))).thenReturn(new PageImpl(products));
        //then
        assertEquals(products.size(), productService.findAllProductsByName(productToBeSearched, PageRequest.of(0, 20)).getTotalElements());

    }

    //UPDATE PRODUCT//
    @Test
    void updateProductSuccess() throws BusinessException {
        //given
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(BigDecimal.valueOf(710.00), Currency.EUR);
        String updatedProductId = UUID.randomUUID().toString();
        Product updatedProduct = Product.builder().name("Iphone 12 Mini").price(BigDecimal.valueOf(700.00)).currency(Currency.EUR).build();
        //when
        when(productRepository.findById(updatedProductId)).thenReturn(Optional.of(updatedProduct));
        //then
        productService.updateProduct(updatedProductId,updateProductRequest);
        assertEquals(updatedProduct.getPrice(), updateProductRequest.getPrice());
    }

    @Test
    void updateNonExistingProductFails() throws BusinessException {
        //given
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(BigDecimal.valueOf(710.00), Currency.EUR);
        String updatedProductId = UUID.randomUUID().toString();
        //when
        when(productRepository.findById(updatedProductId)).thenReturn(Optional.empty());
        //then
        BusinessException exception = assertThrows(BusinessException.class, () -> productService.updateProduct(updatedProductId,updateProductRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorType().getHttpStatus());

        verify(productRepository).findById(updatedProductId);
        verifyNoMoreInteractions(productRepository);
    }
}
