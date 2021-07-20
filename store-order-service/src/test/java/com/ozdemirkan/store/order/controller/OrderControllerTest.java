package com.ozdemirkan.store.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.model.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    public static final String API_ORDER = "/api/order";
    public static final String USER_CREATE_EMAIL = "user_create@example.com";
    public static final String USER_LIST_EMAIL = "user_list@example.com";
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    //CREATE ORDER//
    @Test
    void createOrderSuccess() throws BusinessException, Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(USER_CREATE_EMAIL,
                List.of(new CreateOrderRequest.Product(UUID.randomUUID().toString(),2)));

        mvc.perform(
                post(API_ORDER)
                        .content(mapper.writeValueAsString(createOrderRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createOrderWithInvalidBodyReturns400() throws BusinessException, Exception {

        //Empty order email
        mvc.perform(
                post(API_ORDER)
                        .content(mapper.writeValueAsString(new CreateOrderRequest("",
                                List.of(new CreateOrderRequest.Product(UUID.randomUUID().toString(),2)))))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //null products
        mvc.perform(
                post(API_ORDER)
                        .content(mapper.writeValueAsString(new CreateOrderRequest(USER_CREATE_EMAIL, null)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //FIND ORDER//
    @Test
    void findOrdersByEmailSuccess() throws BusinessException, Exception {

        mvc.perform(post(API_ORDER)
                .content(mapper.writeValueAsString(new CreateOrderRequest(USER_LIST_EMAIL,
                        List.of(
                                new CreateOrderRequest.Product(UUID.randomUUID().toString(),2),
                                new CreateOrderRequest.Product(UUID.randomUUID().toString(),5))
                        )))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(
                get(API_ORDER +"?email="+ USER_LIST_EMAIL))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(status().isOk());
    }

    @Test
    void findOrdersByWrongEmailReturnsEmptyList() throws BusinessException, Exception {

        mvc.perform(
                get(API_ORDER +"?email=user_na@example.com"))
                .andExpect(jsonPath("$.data.totalElements").value(0))
                .andExpect(status().isOk());
    }

    @Test
    void findOrdersWithInvalidNameReturns400() throws BusinessException, Exception {

        mvc.perform(
                get(API_ORDER +"?email="))
                .andExpect(status().isBadRequest());
    }
}
