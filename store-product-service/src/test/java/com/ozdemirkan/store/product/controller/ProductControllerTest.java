package com.ozdemirkan.store.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.Currency;
import com.ozdemirkan.store.product.model.GetProductDetailsRequest;
import com.ozdemirkan.store.product.model.UpdateProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    public static final String API_PRODUCT = "/api/product";
    public static final String API_PRODUCT_DETAILS = "/api/product/details";
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    //CREATE PRODUCT//
    @Test
    void createProductSuccess() throws BusinessException, Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12", BigDecimal.valueOf(700.00), Currency.EUR);

        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(createProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createExistingProductReturns422() throws BusinessException, Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12 Pro", BigDecimal.valueOf(800.00),Currency.EUR);

        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(createProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(createProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void createProductWithInvalidBodyReturns400() throws BusinessException, Exception {

        //Empty product name
        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(new CreateProductRequest("", BigDecimal.valueOf(800.00), Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //null product price
        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(new CreateProductRequest("Iphone 12 Pro", null, Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //minus product price
        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(new CreateProductRequest("Iphone 12 Pro", BigDecimal.valueOf(-800.00), Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //null currency
        mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(new CreateProductRequest("Iphone 12 Pro", BigDecimal.valueOf(800.00), null)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //FIND PRODUCT//
    @Test
    void findProductsByNameSuccess() throws BusinessException, Exception {

        mvc.perform(post(API_PRODUCT)
                .content(mapper.writeValueAsString(new CreateProductRequest("iPad Mini", BigDecimal.valueOf(400.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mvc.perform(post(API_PRODUCT)
                .content(mapper.writeValueAsString(new CreateProductRequest("iPad Air", BigDecimal.valueOf(700.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mvc.perform(post(API_PRODUCT)
                .content(mapper.writeValueAsString(new CreateProductRequest("iPad Pro", BigDecimal.valueOf(800.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(
                get(API_PRODUCT+"?name=iPad"))
                .andExpect(jsonPath("$.data.totalElements").value(3))
                .andExpect(status().isOk());
    }

    @Test
    void findProductsByNameWithWrongNameReturnsEmptyList() throws BusinessException, Exception {

        mvc.perform(
                get(API_PRODUCT+"?name=FlyingCar"))
                .andExpect(jsonPath("$.data.totalElements").value(0))
                .andExpect(status().isOk());
    }

    @Test
    void findProductsWithInvalidNameReturns400() throws BusinessException, Exception {

        mvc.perform(
                get(API_PRODUCT+"?name="))
                .andExpect(status().isBadRequest());
    }

    //UPDATE PRODUCT//
    @Test
    void updateProductSuccess() throws BusinessException, Exception {
        //Create a new product
        String location = (String) mvc.perform(post(API_PRODUCT)
                .content(mapper.writeValueAsString(new CreateProductRequest("iWatch SE", BigDecimal.valueOf(400.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getHeaderValue(HttpHeaders.LOCATION);

        String newProductId = location.substring(location.lastIndexOf("/")+1);

        UpdateProductRequest updateProductRequest   = new UpdateProductRequest(BigDecimal.valueOf(701.00), Currency.EUR);

        mvc.perform(
                patch(API_PRODUCT+"/"+newProductId)
                        .content(mapper.writeValueAsString(updateProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateProductWithUnexistingIdReturns404() throws BusinessException, Exception {
        UpdateProductRequest updateProductRequest   = new UpdateProductRequest(BigDecimal.valueOf(701.00), Currency.EUR);
        String unexistingId = UUID.randomUUID().toString();

        mvc.perform(
                patch(API_PRODUCT+"/"+unexistingId)
                        .content(mapper.writeValueAsString(updateProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProductWithInvalidFormattedIdReturns400() throws BusinessException, Exception {
        UpdateProductRequest updateProductRequest   = new UpdateProductRequest(BigDecimal.valueOf(701.00), Currency.EUR);
        String idWithInvalidFormat = "1234";

        mvc.perform(
                patch(API_PRODUCT+"/"+idWithInvalidFormat)
                        .content(mapper.writeValueAsString(updateProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //GET PRODUCT DETAILS//
    @Test
    void getProductDetailsSuccess() throws BusinessException, Exception {

        String firstProductLocation = (String) mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(new CreateProductRequest("MacBook Air", BigDecimal.valueOf(900.00), Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeaderValue(HttpHeaders.LOCATION);

        String secondProductLocation = (String) mvc.perform(
                post(API_PRODUCT)
                        .content(mapper.writeValueAsString(new CreateProductRequest("MacBook Pro", BigDecimal.valueOf(700.00), Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeaderValue(HttpHeaders.LOCATION);


        mvc.perform(
                post(API_PRODUCT_DETAILS)
                        .content(mapper.writeValueAsString(new GetProductDetailsRequest(
                                List.of(
                                        firstProductLocation.substring(firstProductLocation.lastIndexOf("/") + 1),
                                        secondProductLocation.substring(secondProductLocation.lastIndexOf("/")+ 1)))))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.productDetails", hasSize(2)))
                .andExpect(status().isOk());
    }

}
