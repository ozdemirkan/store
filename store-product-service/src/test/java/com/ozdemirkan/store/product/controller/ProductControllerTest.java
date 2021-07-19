package com.ozdemirkan.store.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void createProductSuccess() throws BusinessException, Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12", BigDecimal.valueOf(700.00), Currency.EUR);

        mvc.perform(
                post("/api/product")
                        .content(mapper.writeValueAsString(createProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createExistingProductReturns422() throws BusinessException, Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("iPhone 12 Pro", BigDecimal.valueOf(800.00),Currency.EUR);

        mvc.perform(
                post("/api/product")
                        .content(mapper.writeValueAsString(createProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isCreated());

        mvc.perform(
                post("/api/product")
                        .content(mapper.writeValueAsString(createProductRequest))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void createProductWithInvalidBodyReturns400() throws BusinessException, Exception {

        //Empty product name
        mvc.perform(
                post("/api/product")
                        .content(mapper.writeValueAsString(new CreateProductRequest("", BigDecimal.valueOf(800.00), Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //null product price
        mvc.perform(
                post("/api/product")
                        .content(mapper.writeValueAsString(new CreateProductRequest("Iphone 12 Pro", null, Currency.EUR)))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //null currency
        mvc.perform(
                post("/api/product")
                        .content(mapper.writeValueAsString(new CreateProductRequest("Iphone 12 Pro", BigDecimal.valueOf(800.00), null)))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void findProductsByNameSuccess() throws BusinessException, Exception {

        mvc.perform(post("/api/product")
                .content(mapper.writeValueAsString(new CreateProductRequest("iPad Mini", BigDecimal.valueOf(400.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/product")
                .content(mapper.writeValueAsString(new CreateProductRequest("iPad Air", BigDecimal.valueOf(700.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/product")
                .content(mapper.writeValueAsString(new CreateProductRequest("iPad Pro", BigDecimal.valueOf(800.00), Currency.EUR)))
                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(status().isCreated());

        mvc.perform(
                get("/api/product?name=iPad"))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(status().isOk());
    }

    @Test
    void findProductsByNameWithWrongNameReturnsEmptyList() throws BusinessException, Exception {

        mvc.perform(
                get("/api/product?name=FlyingCar"))
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    void findProductsWithInvalidNameReturns400() throws BusinessException, Exception {

        mvc.perform(
                get("/api/product?name="))
                .andExpect(status().isBadRequest());
    }
}
