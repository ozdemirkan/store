package com.ozdemirkan.store.product.service;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.Currency;
import com.ozdemirkan.store.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

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
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12", BigDecimal.valueOf(700.00),Currency.EUR);
        //when
        when(productRepository.findByName(createProductRequest.getName())).thenReturn(Optional.of(Product.builder().name("Iphone 12").price(BigDecimal.valueOf(700.00)).build()));
        //then
        BusinessException exception = assertThrows(BusinessException.class, () -> productService.createProduct(createProductRequest));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getErrorType().getHttpStatus());

        verify(productRepository).findByName(createProductRequest.getName());
        verifyNoMoreInteractions(productRepository);
    }
}
